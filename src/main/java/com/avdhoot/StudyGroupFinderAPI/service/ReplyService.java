package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.dto.answerQuery.ReplyRequest;
import com.avdhoot.StudyGroupFinderAPI.dto.answerQuery.ReplyResponse;
import com.avdhoot.StudyGroupFinderAPI.entity.*;
import com.avdhoot.StudyGroupFinderAPI.exception.EntityAndRelationshipsFinder;
import com.avdhoot.StudyGroupFinderAPI.exception.ResourceNotFoundException;
import com.avdhoot.StudyGroupFinderAPI.mapper.QueryMapper;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupMembershipRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.queryRepository.ReplyRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.queryRepository.GroupQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final QueryMapper queryMapper;
    private final GroupMembershipRepository groupMembershipRepository;
    private final EntityAndRelationshipsFinder entityAndRelationshipsFinder;
    private final GroupQueryRepository groupQueryRepository;

    public ReplyResponse createReply(int groupId, int questionQueryId, ReplyRequest request, int replyCreatorId) {

        Group group = entityAndRelationshipsFinder.getGroupOrThrow(groupId);
        Query query = entityAndRelationshipsFinder.getQueryInGroupOrThrow(groupId,questionQueryId);
        User user = entityAndRelationshipsFinder.getUserOrThrow(replyCreatorId);

        if(!groupMembershipRepository.existsByGroupAndUser(group, user)) {
            throw  new ResourceNotFoundException(" User with ID:" + user.getId() + " does not belong to "+ group.getGroupName()+" group. Please join the Group before posting Reply");
        }

        Reply reply = Reply
                .builder()
                .content(request.content())
                .user(user)
                .query(query)
                .group(group).createdAt(LocalDate.now())
                .build();
        replyRepository.save(reply);

        return queryMapper.toAnswerQueryResponse(reply);
    }

    public Page<ReplyResponse> getAllSolutions(Pageable pageable, int groupId, int queryId) {
        Group group = entityAndRelationshipsFinder.getGroupOrThrow(groupId);
        Query query = entityAndRelationshipsFinder.getQueryInGroupOrThrow(groupId, queryId);

        Page<Reply> replies = replyRepository.findByGroup_IdAndQuery_QueryId(
                group.getId(),
                query.getQueryId(),
                pageable);

        return replies.map(queryMapper::toAnswerQueryResponse);
    }

    public void deleteReply(Integer groupId, Integer queryId, Integer replyId) {
        Reply reply = entityAndRelationshipsFinder.getReplyInQueryAndGroupOrThrow(groupId, queryId, replyId);
        replyRepository.delete(reply);
    }
}

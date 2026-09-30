package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.dto.answerQuery.ReplyRequest;
import com.avdhoot.StudyGroupFinderAPI.dto.answerQuery.ReplyResponse;
import com.avdhoot.StudyGroupFinderAPI.entity.Group;
import com.avdhoot.StudyGroupFinderAPI.entity.Query;
import com.avdhoot.StudyGroupFinderAPI.entity.Reply;
import com.avdhoot.StudyGroupFinderAPI.entity.User;
import com.avdhoot.StudyGroupFinderAPI.exception.EntityAndRelationshipsFinder;
import com.avdhoot.StudyGroupFinderAPI.mapper.QueryMapper;
import com.avdhoot.StudyGroupFinderAPI.repository.queryRepository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final AnswerRepository answerRepository;
    private final QueryMapper queryMapper;
    private final EntityAndRelationshipsFinder entityAndRelationshipsFinder;

    public ReplyResponse createReply(int groupId, int questionQueryId, ReplyRequest request) {

        Group group = entityAndRelationshipsFinder.getGroupOrThrow(groupId);
        Query query = entityAndRelationshipsFinder.getQueryInGroupOrThrow(questionQueryId, groupId);
        User user = entityAndRelationshipsFinder.getUserOrThrow(request.memberId());

        Reply reply = Reply
                .builder()
                .content(request.content())
                .user(user)
                .query(query)
                .group(group).createdAt(LocalDate.now())
                .build();
        answerRepository.save(reply);

        return queryMapper.toAnswerQueryResponse(reply);
    }

    public Page<ReplyResponse> getAllSolutions(Pageable pageable, int groupId, int queryId) {

        Group group = entityAndRelationshipsFinder.getGroupOrThrow(groupId);

        Query query = entityAndRelationshipsFinder.getQueryInGroupOrThrow(groupId, queryId);

        entityAndRelationshipsFinder.getQueryInGroupOrThrow(query.getQueryId(), group.getId());

        Page<Reply> replies = answerRepository.findByGroup_IdAndQuery_QueryId( groupId,  queryId, pageable);

        return replies.map(queryMapper::toAnswerQueryResponse);
    }
}

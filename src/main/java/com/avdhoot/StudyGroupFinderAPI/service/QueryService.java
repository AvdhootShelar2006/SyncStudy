package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.entity.*;
import com.avdhoot.StudyGroupFinderAPI.exception.EntityAndRelationshipsFinder;
import com.avdhoot.StudyGroupFinderAPI.exception.ResourceNotFoundException;
import com.avdhoot.StudyGroupFinderAPI.mapper.QueryMapper;
import com.avdhoot.StudyGroupFinderAPI.dto.queryDto.CreateQueryRequest;
import com.avdhoot.StudyGroupFinderAPI.dto.queryDto.CreateQueryResponse;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupMembershipRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.queryRepository.AnswerRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.queryRepository.GroupQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class QueryService {

    private final GroupQueryRepository groupQueryRepository;
    private final AnswerRepository answerRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    private final QueryMapper queryMapper;
    private final EntityAndRelationshipsFinder entityAndRelationshipsFinder;


    public CreateQueryResponse postQuery(int groupId,CreateQueryRequest request ,int creatorId) {
        User user = entityAndRelationshipsFinder.getUserOrThrow(creatorId);
        Group group = entityAndRelationshipsFinder.getGroupOrThrow(groupId);

        if(!groupMembershipRepository.existsByGroupAndUser(group, user)) {
            throw  new ResourceNotFoundException(" User with ID:" + creatorId + " does not belong to "+ group.getGroupName()+" group. Please join the Group before posting Query");
        }
        Query newQuery = Query
                .builder()
                .title(request.title())
                .description(request.description())
                .postedBy(user)
                .group(group)
                .isResolved(false)
                .createdAt(LocalDate.now())
                .build();
        groupQueryRepository.save(newQuery);

        return queryMapper.toCreateQueryResponse(newQuery);
    }

    public Page<CreateQueryResponse> getAllGroupQueries( Pageable pageable , int groupId) {

        Group group = entityAndRelationshipsFinder.getGroupOrThrow(groupId);

        Page<Query> queries = groupQueryRepository.findByGroup_Id(pageable, group.getId());

        return queries.map(queryMapper::toCreateQueryResponse);
    }

    public CreateQueryResponse getGroupQueryById(int questionQueryId, int groupId) {
        Query query = entityAndRelationshipsFinder.getQueryInGroupOrThrow(groupId,questionQueryId);

        return queryMapper.toCreateQueryResponse(query);
    }

    public CreateQueryResponse resolveGroupQuery(int questionQueryId, int groupId, boolean resolve) {
        Query query = entityAndRelationshipsFinder.getQueryInGroupOrThrow(groupId, questionQueryId);
        query.setResolved(resolve);

        CreateQueryResponse queryResponse = queryMapper.toCreateQueryResponse(query);
        groupQueryRepository.save(query);

        return queryResponse;
    }

}


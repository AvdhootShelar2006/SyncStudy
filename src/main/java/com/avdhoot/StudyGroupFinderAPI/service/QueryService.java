package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.mapper.QueryMapper;
import com.avdhoot.StudyGroupFinderAPI.dto.answerQuery.AnswerQueryRequest;
import com.avdhoot.StudyGroupFinderAPI.dto.answerQuery.AnswerQueryResponse;
import com.avdhoot.StudyGroupFinderAPI.dto.queryDto.QueryRequest;
import com.avdhoot.StudyGroupFinderAPI.entity.Member;
import com.avdhoot.StudyGroupFinderAPI.entity.StudyGroup;
import com.avdhoot.StudyGroupFinderAPI.dto.queryDto.GroupQueryResponse;
import com.avdhoot.StudyGroupFinderAPI.entity.AnswerQuery;
import com.avdhoot.StudyGroupFinderAPI.entity.GroupQuery;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupMembershipRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.queryRepository.AnswerRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.queryRepository.GroupQueryRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryService {

    private final GroupQueryRepository groupQueryRepository;
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    private final QueryMapper queryMapper;



    public void postQuery(int groupId, QueryRequest request) {
        Member member = memberRepository.findById(request.memberId()).orElseThrow();
        StudyGroup group = groupRepository.findById(groupId).orElseThrow();

        if(groupMembershipRepository.existsByGroupAndMember(group, member)) {
            GroupQuery newQuery = GroupQuery
                    .builder()
                    .title(request.title())
                    .description(request.description())
                    .postedBy(member)
                    .studyGroup(group)
                    .isResolved(false)
                    .createdAt(LocalDate.now()).build();
            groupQueryRepository.save(newQuery);
        }
    }

    public List<GroupQueryResponse> getAllGroupQueries(Integer groupId) {
        getGroupOrThrow(groupId);
        List<GroupQuery> queries = groupQueryRepository.findByStudyGroup_Id(groupId);
        return queryMapper.toGroupQueryResponses(queries);
    }

    public GroupQueryResponse getGroupQuery(int queryId, int groupId) {
        GroupQuery query = getQueryInGroupOrThrow(queryId, groupId);
        return queryMapper.toGroupQueryResponse(query);
    }

    public GroupQueryResponse resolveGroupQuery(int queryId, int groupId) {
        GroupQuery query = getQueryInGroupOrThrow(queryId, groupId);
        query.setResolved(true);
        groupQueryRepository.save(query);
        return queryMapper.toGroupQueryResponse(query);
    }

    public AnswerQueryResponse answerQuery(int groupId,int queryId, AnswerQueryRequest request) {

        StudyGroup group = getGroupOrThrow(groupId);
        GroupQuery groupQuery = getQueryInGroupOrThrow(queryId, groupId);
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new RuntimeException("Member Not Found! (404)"));
        AnswerQuery answerQuery = AnswerQuery
                .builder()
                .content(request.content())
                .user(member)
                .groupQuery(groupQuery)
                .studyGroup(group).createdAt(LocalDate.now())
                .build();
        answerRepository.save(answerQuery);
        return queryMapper.toAnswerQueryResponse(answerQuery);
    }

    public List<AnswerQueryResponse> getAllSolutions(int groupId, int queryId) {
        getGroupOrThrow(groupId);
        getQueryInGroupOrThrow(queryId, groupId);
        List<AnswerQuery> answerQueries = answerRepository.findSolutions(groupId, queryId);
        return queryMapper.toAnswerQueryResponses(answerQueries);
    }


    private StudyGroup getGroupOrThrow(int groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group Not Found"));
    }

    private GroupQuery getQueryInGroupOrThrow(int queryId, int groupId) {
        GroupQuery query = groupQueryRepository.findById(queryId)
                .orElseThrow(() -> new RuntimeException("Query Not Found!!"));
        if (!query.getStudyGroup().getId().equals(groupId)) {
            throw new RuntimeException("Query does not belong to this group");
        }
        return query;
    }
}


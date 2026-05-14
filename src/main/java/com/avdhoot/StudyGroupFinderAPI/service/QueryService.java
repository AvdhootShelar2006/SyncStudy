package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.model.dto.answer_query.AnswerQueryRequest;
import com.avdhoot.StudyGroupFinderAPI.model.dto.answer_query.AnswerQueryResponse;
import com.avdhoot.StudyGroupFinderAPI.model.dto.query_dto.QueryRequest;
import com.avdhoot.StudyGroupFinderAPI.model.entities.Member;
import com.avdhoot.StudyGroupFinderAPI.model.entities.StudyGroup;
import com.avdhoot.StudyGroupFinderAPI.model.dto.query_dto.GroupQueryResponse;
import com.avdhoot.StudyGroupFinderAPI.model.interaction.AnswerQuery;
import com.avdhoot.StudyGroupFinderAPI.model.interaction.GroupQuery;
import com.avdhoot.StudyGroupFinderAPI.repository.queryRepository.AnswerRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.queryRepository.GroupQueryRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class QueryService {

    @Autowired
    private GroupQueryRepository groupQueryRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AnswerRepository answerRepository;
    /*

    public void createQuery(GroupQuery inputQuery) {
        Integer memberId = inputQuery.getPostedBy().getId();
        Integer groupId = inputQuery.getStudyGroup().getId();

        StudyGroup group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group Not Found"));

        Member realMember = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Invalid Member"));

        inputQuery.setStudyGroup(group);
        inputQuery.setPostedBy(realMember);

        groupQueryRepository.save(inputQuery);
    }
    */

    public List<GroupQueryResponse> getAllGroupQueris(Integer groupId) {

        StudyGroup group = groupRepository
                .findById(groupId)
                .orElseThrow(() -> new RuntimeException("404"));

        List<GroupQueryResponse> responses = new ArrayList<>();

        List<GroupQuery> queries = groupQueryRepository.findByStudyGroup_Id(groupId);

        for(GroupQuery query : queries){
            GroupQueryResponse response = new GroupQueryResponse(
                    query.getTitle(),
                    query.getDescription(),
                    query.getPostedBy().getName(),
                    query.getStudyGroup().getId(),
                    query.isResolved()
            );
            responses.add(response);
        }
        return responses;
    }

    public GroupQueryResponse getGroupQuery(int qId, int groupId) {
        StudyGroup group = groupRepository
                .findById(groupId)
                .orElseThrow(() -> new RuntimeException("404"));

        GroupQuery query = groupQueryRepository.findById(qId)
                .orElseThrow(() -> new RuntimeException("Query Not Found!!"));

        GroupQueryResponse response = new GroupQueryResponse(
                query.getTitle(),
                query.getDescription(),
                query.getPostedBy().getName(),
                query.getStudyGroup().getId(),
                query.isResolved());
        return response;
    }

    public GroupQueryResponse resolveGroupQuery(int qId, int groupId) {

        StudyGroup group = groupRepository
                .findById(groupId)
                .orElseThrow(() -> new RuntimeException("404"));

        GroupQuery query = groupQueryRepository.findById(qId)
                .orElseThrow(() -> new RuntimeException("Query Not Found!!"));
        query.setResolved(true);

        GroupQueryResponse response = new GroupQueryResponse(
                query.getTitle(),
                query.getDescription(),
                query.getPostedBy().getName(),
                query.getStudyGroup().getId(),
                query.isResolved());

        groupQueryRepository.save(query);
        return response;
    }


    public AnswerQueryResponse answerQuery(int groupId,int queryId, AnswerQueryRequest request) {

        StudyGroup group = groupRepository
                .findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group Not Found"));

        GroupQuery groupQuery = groupQueryRepository
                .findById(queryId)
                .orElseThrow(()-> new RuntimeException(("Query does not exist")));

        Member member = memberRepository
                .findById(request.memberId())
                .orElseThrow(()-> new RuntimeException("Member Not Found! (404)"));

        AnswerQuery answerQuery = new AnswerQuery();
        answerQuery.setContent(request.content());
        answerQuery.setUser(member);
        answerQuery.setGroupQuery(groupQuery);
        answerQuery.setStudyGroup(group);
        answerQuery.setCreatedAt(LocalDate.now());


        answerRepository.save(answerQuery);

        return new AnswerQueryResponse(
                answerQuery.getContent(),
                member.getName(),
                groupQuery.getTitle()
        );
    }

    public List<AnswerQueryResponse> getAllSolutions(int groupId, int queryId) {

        StudyGroup group = groupRepository
                .findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group Not Found"));

        GroupQuery groupQuery = groupQueryRepository
                .findById(queryId)
                .orElseThrow(()-> new RuntimeException(("Query does not exist")));

        List<AnswerQuery> queries = answerRepository.findSolutions(groupId, queryId);

        List<AnswerQueryResponse> responses = new ArrayList<>();

        for( AnswerQuery query : queries){
            AnswerQueryResponse queryResponse = new AnswerQueryResponse(
                    query.getContent(),
                    groupQuery.getPostedBy().getName(),
                    groupQuery.getTitle()
            );
            responses.add(queryResponse);
        }
        return responses;
    }

    public void postQuery(int groupId, QueryRequest request) {
        GroupQuery newQuery = new GroupQuery();
        newQuery.setTitle(request.title());
        newQuery.setDescription(request.description());
        newQuery.setResolved(false);
        newQuery.setCreatedAt(LocalDate.now());

        Member member = memberRepository.findById(request.memberId()).orElseThrow();
        StudyGroup group = groupRepository.findById(groupId).orElseThrow();

        newQuery.setPostedBy(member);
        newQuery.setStudyGroup(group);

        groupQueryRepository.save(newQuery);
    }
}


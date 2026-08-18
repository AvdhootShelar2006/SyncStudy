package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.model.dto.answer_query.AnswerQueryResponse;
import com.avdhoot.StudyGroupFinderAPI.model.dto.query_dto.GroupQueryResponse;
import com.avdhoot.StudyGroupFinderAPI.model.dto.answer_query.AnswerQueryRequest;
import com.avdhoot.StudyGroupFinderAPI.model.dto.query_dto.QueryRequest;
import com.avdhoot.StudyGroupFinderAPI.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class QueryController {

    @Autowired
    private QueryService queryService;

    @PostMapping("/groups/{id}/queries")
    public ResponseEntity<?> createQuery(
            @PathVariable("id") int groupId,
            @RequestBody QueryRequest request
            ){
        try{
            queryService.postQuery(groupId, request);
            return new ResponseEntity<>( HttpStatus.CREATED);
        }
        catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/groups/{id}/queries")
    public ResponseEntity<List<GroupQueryResponse>> getAllGroupQueries(
            @PathVariable("id") Integer groupId
    ){

        List<GroupQueryResponse> queries = queryService.getAllGroupQueris(groupId);

        return new ResponseEntity<>(queries, HttpStatus.FOUND);
    }

    @GetMapping("/groups/{id}/queries/{qId}")
    public ResponseEntity<GroupQueryResponse> getGroupQuery(
            @PathVariable("qId") int qId,
            @PathVariable("id") int groupId
    ) {

        GroupQueryResponse queryById = queryService.getGroupQuery(qId, groupId);

        return new ResponseEntity<>(queryById , HttpStatus.FOUND);
    }

    @PatchMapping("/groups/{id}/queries/{qId}/resolve")
    public ResponseEntity<GroupQueryResponse> resolveQuery(
            @PathVariable("qId") int qId,
            @PathVariable("id") int groupId
    ) {

        GroupQueryResponse queryById = queryService.resolveGroupQuery(qId, groupId);

        return new ResponseEntity<>(queryById , HttpStatus.OK);
    }

    // Answer Query

    @PostMapping("/groups/{id}/queries/{qId}/answers")
    public ResponseEntity<AnswerQueryResponse> answerQuery(
            @PathVariable("id") int groupId,
            @PathVariable("qId") int queryId,
            @RequestBody AnswerQueryRequest request){

        AnswerQueryResponse response = queryService.answerQuery(groupId, queryId,request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/groups/{id}/queries/{qId}/answers")
    public ResponseEntity<List<AnswerQueryResponse>> getAllSolutions(
            @PathVariable("id") int groupId,
            @PathVariable("qId") int queryId
    ){
        List<AnswerQueryResponse> answerQueries = queryService.getAllSolutions(groupId, queryId);

        return new ResponseEntity<>(answerQueries, HttpStatus.FOUND);
    }
}

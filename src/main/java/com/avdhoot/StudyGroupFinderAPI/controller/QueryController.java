package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.dto.answerQuery.AnswerQueryResponse;
import com.avdhoot.StudyGroupFinderAPI.dto.queryDto.CreateQueryResponse;
import com.avdhoot.StudyGroupFinderAPI.dto.answerQuery.AnswerQueryRequest;
import com.avdhoot.StudyGroupFinderAPI.dto.queryDto.CreateQueryRequest;
import com.avdhoot.StudyGroupFinderAPI.service.QueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class QueryController {

    private final QueryService queryService;

    @PostMapping("/groups/{groupId}/queries")
    public ResponseEntity<CreateQueryResponse> createQuery(
           @PathVariable("groupId") Integer groupId,
           @Valid @RequestBody CreateQueryRequest request
            ){
        CreateQueryResponse response = queryService.postQuery(groupId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/groups/{groupId}/queries")
    public ResponseEntity<List<CreateQueryResponse>> getAllGroupQueries(
            @PathVariable("groupId") Integer groupId
    ){
        List<CreateQueryResponse> queries = queryService.getAllGroupQueries(groupId);
        return new ResponseEntity<>(queries, HttpStatus.FOUND);
    }

    @GetMapping("/groups/{groupId}/queries/{queryId}")
    public ResponseEntity<CreateQueryResponse> getGroupQuery(
            @PathVariable("groupId") Integer groupId,
            @PathVariable("queryId") Integer queryId
    ) {
        CreateQueryResponse queryById = queryService.getGroupQuery(queryId, groupId);
        return new ResponseEntity<>(queryById , HttpStatus.FOUND);
    }

    @PatchMapping("/groups/{groupId}/queries/{queryId}/resolve")
    public ResponseEntity<CreateQueryResponse> resolveQuery(
           @PathVariable("groupId") Integer groupId,
           @PathVariable("queryId") Integer queryId
    ) {
        CreateQueryResponse queryById = queryService.resolveGroupQuery(queryId, groupId);
        return new ResponseEntity<>(queryById , HttpStatus.OK);
    }

    // Answer Query
    @PostMapping("/groups/{groupId}/queries/{queryId}/solutions")
    public ResponseEntity<AnswerQueryResponse> answerQuery(
            @PathVariable("groupId") Integer groupId,
            @PathVariable("queryId") Integer queryId,
            @Valid @RequestBody AnswerQueryRequest request){

        AnswerQueryResponse response = queryService.answerQuery(groupId, queryId,request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/groups/{groupId}/queries/{queryId}/solutions")
    public ResponseEntity<List<AnswerQueryResponse>> getAllSolutions(
            @PathVariable("groupId") Integer groupId,
            @PathVariable("queryId") Integer queryId
    ){
        List<AnswerQueryResponse> answerQueries = queryService.getAllSolutions(groupId, queryId);
        return new ResponseEntity<>(answerQueries, HttpStatus.FOUND);
    }
}

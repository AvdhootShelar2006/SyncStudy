package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.dto.answerQuery.AnswerQueryResponse;
import com.avdhoot.StudyGroupFinderAPI.dto.queryDto.GroupQueryResponse;
import com.avdhoot.StudyGroupFinderAPI.dto.answerQuery.AnswerQueryRequest;
import com.avdhoot.StudyGroupFinderAPI.dto.queryDto.QueryRequest;
import com.avdhoot.StudyGroupFinderAPI.service.QueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("groupmate")
@RequiredArgsConstructor
public class QueryController {

    private final QueryService queryService;

    @PostMapping("/createQuery")
    public ResponseEntity<?> createQuery(
            @RequestParam int groupId,
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

    @GetMapping("/queries")
    public ResponseEntity<List<GroupQueryResponse>> getAllGroupQueries(
            @RequestParam Integer groupId
    ){
        List<GroupQueryResponse> queries = queryService.getAllGroupQueries(groupId);
        return new ResponseEntity<>(queries, HttpStatus.FOUND);
    }

    @GetMapping("/groupQuery")
    public ResponseEntity<GroupQueryResponse> getGroupQuery(
            @RequestParam int queryId,
            @RequestParam int groupId
    ) {
        GroupQueryResponse queryById = queryService.getGroupQuery(queryId, groupId);
        return new ResponseEntity<>(queryById , HttpStatus.FOUND);
    }

    @PatchMapping("/resolve")
    public ResponseEntity<GroupQueryResponse> resolveQuery(
           @RequestParam int queryId,
           @RequestParam int groupId
    ) {
        GroupQueryResponse queryById = queryService.resolveGroupQuery(queryId, groupId);
        return new ResponseEntity<>(queryById , HttpStatus.OK);
    }

    // Answer Query
    @PostMapping("/solutions")
    public ResponseEntity<AnswerQueryResponse> answerQuery(
            @RequestParam int groupId,
            @RequestParam int queryId,
            @RequestBody AnswerQueryRequest request){

        AnswerQueryResponse response = queryService.answerQuery(groupId, queryId,request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/solutions")
    public ResponseEntity<List<AnswerQueryResponse>> getAllSolutions(
            @RequestParam int groupId,
            @RequestParam int queryId
    ){
        List<AnswerQueryResponse> answerQueries = queryService.getAllSolutions(groupId, queryId);
        return new ResponseEntity<>(answerQueries, HttpStatus.FOUND);
    }
}

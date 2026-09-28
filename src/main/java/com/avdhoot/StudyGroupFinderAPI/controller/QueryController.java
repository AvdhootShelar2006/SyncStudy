package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.dto.queryDto.CreateQueryResponse;
import com.avdhoot.StudyGroupFinderAPI.dto.queryDto.CreateQueryRequest;
import com.avdhoot.StudyGroupFinderAPI.service.QueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<CreateQueryResponse>> getAllGroupQueries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable("groupId") int groupId
    ){
        Pageable pageable = PageRequest.of(page, size);

        Page<CreateQueryResponse> queries = queryService.getAllGroupQueries(pageable, groupId);
        return new ResponseEntity<>(queries, HttpStatus.FOUND);
    }

    @GetMapping("/groups/{groupId}/queries/{queryId}")
    public ResponseEntity<CreateQueryResponse> getGroupQueryById(
            @PathVariable("groupId") Integer groupId,
            @PathVariable("queryId") Integer queryId
    ) {
        CreateQueryResponse queryById = queryService.getGroupQueryById(queryId, groupId);
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

}

package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.dto.queryDto.CreateQueryResponse;
import com.avdhoot.StudyGroupFinderAPI.dto.queryDto.CreateQueryRequest;
import com.avdhoot.StudyGroupFinderAPI.entity.CustomUserDetails;
import com.avdhoot.StudyGroupFinderAPI.service.QueryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class QueryController {

    private final QueryService queryService;

    @PostMapping("/groups/{groupId}/query")
    public ResponseEntity<CreateQueryResponse> createQuery(
            @PathVariable("groupId") Integer groupId,
            @Valid @RequestBody CreateQueryRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        CreateQueryResponse response = queryService.postQuery(groupId, request, userDetails.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/groups/{groupId}/query")
    public ResponseEntity<Page<CreateQueryResponse>> getAllGroupQueries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable("groupId") int groupId
    ){
        Pageable pageable = PageRequest.of(page, size);

        Page<CreateQueryResponse> queries = queryService.getAllGroupQueries(pageable, groupId);
        return new ResponseEntity<>(queries, HttpStatus.OK);
    }

    @GetMapping("/groups/{groupId}/query/{queryId}")
    public ResponseEntity<CreateQueryResponse> getGroupQueryById(
            @PathVariable("groupId") Integer groupId,
            @PathVariable("queryId") Integer queryId
    ) {
        CreateQueryResponse queryById = queryService.getGroupQueryById(queryId, groupId);
        return new ResponseEntity<>(queryById , HttpStatus.OK);
    }

    @PreAuthorize("@groupSecurityConfig.isQueryOwner(authentication, #queryId)")
    @PatchMapping("/groups/{groupId}/query/{queryId}/resolve")
    public ResponseEntity<CreateQueryResponse> resolveQuery(
           @PathVariable("groupId") Integer groupId,
           @PathVariable("queryId") Integer queryId,
           @RequestParam boolean resolve
    ) {
        CreateQueryResponse queryById = queryService.resolveGroupQuery(queryId, groupId,resolve);
        return new ResponseEntity<>(queryById , HttpStatus.OK);
    }

}

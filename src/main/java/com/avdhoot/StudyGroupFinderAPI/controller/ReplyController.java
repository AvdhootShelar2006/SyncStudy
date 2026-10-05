package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.dto.answerQuery.ReplyRequest;
import com.avdhoot.StudyGroupFinderAPI.dto.answerQuery.ReplyResponse;
import com.avdhoot.StudyGroupFinderAPI.entity.CustomUserDetails;
import com.avdhoot.StudyGroupFinderAPI.service.ReplyService;
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
public class ReplyController {

    private final ReplyService replyServiceService;

    @PostMapping("/groups/{groupId}/query/{queryId}")
    public ResponseEntity<ReplyResponse> createReply(
            @PathVariable("groupId") Integer groupId,
            @PathVariable("queryId") Integer queryId,
            @Valid @RequestBody ReplyRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        ReplyResponse response = replyServiceService.createReply(groupId, queryId,request, userDetails.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("@groupSecurityConfig.isGroupMember(authentication, #groupId)")
    @GetMapping("/groups/{groupId}/query/{queryId}/solutions")
    public ResponseEntity<Page<ReplyResponse>> getAllSolutions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable("groupId") Integer groupId,
            @PathVariable("queryId") Integer queryId
    ){

        Pageable pageable = PageRequest.of(page, size);

        Page<ReplyResponse> answerQueries = replyServiceService.getAllSolutions(pageable, groupId, queryId);
        return new ResponseEntity<>(answerQueries, HttpStatus.OK);
    }


}

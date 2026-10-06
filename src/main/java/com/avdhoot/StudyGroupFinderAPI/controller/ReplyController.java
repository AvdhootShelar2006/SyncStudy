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

    private final ReplyService replyService;

    @PostMapping("/groups/{groupId}/query/{queryId}")
    public ResponseEntity<ReplyResponse> createReply(
            @PathVariable("groupId") Integer groupId,
            @PathVariable("queryId") Integer queryId,
            @Valid @RequestBody ReplyRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        ReplyResponse response = replyService.createReply(groupId, queryId,request, userDetails.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("""
    @groupSecurityConfig.hasRole(authentication, #groupId, T(com.avdhoot.StudyGroupFinderAPI.enums.GroupRole).MEMBER)|| 
    @groupSecurityConfig.hasRole(authentication, #groupId, T(com.avdhoot.StudyGroupFinderAPI.enums.GroupRoleGroupRole).OWNER)
    """)
    @GetMapping("/groups/{groupId}/query/{queryId}/solutions")
    public ResponseEntity<Page<ReplyResponse>> getAllSolutions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable("groupId") Integer groupId,
            @PathVariable("queryId") Integer queryId
    ){

        Pageable pageable = PageRequest.of(page, size);

        Page<ReplyResponse> answerQueries = replyService.getAllSolutions(pageable, groupId, queryId);
        return new ResponseEntity<>(answerQueries, HttpStatus.OK);
    }


    @PreAuthorize("@groupSecurityConfig.isReplyOwner(authentication, #replyId)")
    @DeleteMapping("/group/{groupId}/query/{queryId}/reply/{replyId}")
    public ResponseEntity<Void> deleteReply(
            @PathVariable("groupId") Integer groupId,
            @PathVariable("queryId") Integer queryId,
            @PathVariable ("replyId") Integer replyId)
    {
        replyService.deleteReply(groupId, queryId, replyId);
        return ResponseEntity.noContent().build();
    }

}

package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.dto.answerQuery.ReplyRequest;
import com.avdhoot.StudyGroupFinderAPI.dto.answerQuery.ReplyResponse;
import com.avdhoot.StudyGroupFinderAPI.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("api")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyServiceService;


    @PostMapping("/groups/{groupId}/query/{queryId}/solutions")
    public ResponseEntity<ReplyResponse> createReply(
            @PathVariable("groupId") Integer groupId,
            @PathVariable("queryId") Integer queryId,
            @Valid @RequestBody ReplyRequest request){

        ReplyResponse response = replyServiceService.createReply(groupId, queryId,request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/groups/{groupId}/query/{queryId}/solutions")
    public ResponseEntity<Page<ReplyResponse>> getAllSolutions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable("groupId") Integer groupId,
            @PathVariable("queryId") Integer queryId
    ){

        Pageable pageable = PageRequest.of(page, size);

        Page<ReplyResponse> answerQueries = replyServiceService.getAllSolutions(pageable, groupId, queryId);
        return new ResponseEntity<>(answerQueries, HttpStatus.FOUND);
    }
}

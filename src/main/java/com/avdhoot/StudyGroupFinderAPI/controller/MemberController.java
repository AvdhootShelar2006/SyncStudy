package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.dto.memberDto.CreateMemberDetailResponse;
import com.avdhoot.StudyGroupFinderAPI.dto.memberDto.CreateMemberRequestDto;
import com.avdhoot.StudyGroupFinderAPI.service.MemberService;
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
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member")
    public ResponseEntity<CreateMemberDetailResponse> createMember(
            @Valid @RequestBody CreateMemberRequestDto requestDto
    ) {
        return new ResponseEntity<>(memberService.createUser(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<CreateMemberDetailResponse> getMembersId(
            @PathVariable int memberId
    ){
        return ResponseEntity.ok(memberService.getMemberById(memberId));
    }

    @GetMapping("/members")
    public ResponseEntity<Page<CreateMemberDetailResponse>> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<CreateMemberDetailResponse> createMemberDetailResponses = memberService.getAllMembers(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(createMemberDetailResponses);
    }

}

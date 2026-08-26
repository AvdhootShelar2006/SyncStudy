package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.dto.memberDto.CreateMemberDetailResponse;
import com.avdhoot.StudyGroupFinderAPI.dto.memberDto.CreateMemberRequestDto;
import com.avdhoot.StudyGroupFinderAPI.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("groupmate")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/{id}")
    public ResponseEntity<CreateMemberDetailResponse> getMembersId(
            @PathVariable("id") int memberId
    ){

        CreateMemberDetailResponse response = null;

        if(memberId > 0){
            response = memberService.getMemberById(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/member")
    public ResponseEntity<CreateMemberDetailResponse> createMember(@RequestBody CreateMemberRequestDto requestDto) {
        return new ResponseEntity<>(memberService.createUser(requestDto), HttpStatus.CREATED);
    }
}

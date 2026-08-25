package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.model.dto.groupMemberDto.MemberDetailsResponse;
import com.avdhoot.StudyGroupFinderAPI.model.entity.Member;
import com.avdhoot.StudyGroupFinderAPI.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("groupmate")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/{id}")
    public ResponseEntity<MemberDetailsResponse> getMembersId(
            @PathVariable("id") int memberId
    ){

        MemberDetailsResponse response = null;

        if(memberId > 0){
            response = memberService.getMemberById(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/member")
    public ResponseEntity<?> createMember(@RequestBody List<Member> member) {
        List<Member> addMembers = null;
        try{
            addMembers = memberService.addOrUpdateMember(member);
            return new ResponseEntity<>(addMembers, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

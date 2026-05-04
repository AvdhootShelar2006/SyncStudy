package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.model.entities.Member;
import com.avdhoot.StudyGroupFinderAPI.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class MemberController {

    @Autowired
    private MemberService memberService;


    @GetMapping("/member/{id}")
    public ResponseEntity<Member> getMembersId(@PathVariable("id") int memberId){
        Member getMember = memberService.getAllMemberById(memberId);
        if(getMember.getId() > 0){
        return new ResponseEntity<>(getMember, HttpStatus.OK);
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

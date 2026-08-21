package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.model.dto.group_member_dto.*;
import com.avdhoot.StudyGroupFinderAPI.model.entities.StudyGroup;
import com.avdhoot.StudyGroupFinderAPI.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("groupmate")
public class GroupController {

    @Autowired
    private GroupService service;


    // Create Group
    @PostMapping("/groups")
    public ResponseEntity<?> createGroup(@RequestBody StudyGroup studyGroup){

        StudyGroup group = null;

        try{
            group = service.createGroup(studyGroup);
            return new ResponseEntity<>(group, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping("/group/update/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable("id") int groupId, @RequestBody StudyGroup group){
        StudyGroup studyGroup = null;
        try{
            group.setId(groupId);
            studyGroup =  service.updateGroup(groupId, group);
            return new ResponseEntity<>(studyGroup, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
    }

    //Get All Groups
    @GetMapping("/groups")
    public ResponseEntity<List<StudyGroup>> getALlGroups(){

        return new ResponseEntity<>(service.getAllGroups(), HttpStatus.OK);

    }

    //Get Group By Id
    @GetMapping("/groups/{id}")
    public ResponseEntity<StudyGroup> getALlGroupById(@PathVariable("id") int groupId){

        StudyGroup groupById = service.getGroupById(groupId);

        if(groupById.getId() > 0){
            return new ResponseEntity<>(groupById, HttpStatus.FOUND);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // Join Group
    @PostMapping("/groups/{id}/join")
    public ResponseEntity<?> joinGroup(
            @PathVariable("id") int groupId, @RequestBody List<JoinLeaveRequest> request){

       service.joinGroup(groupId, request);

        return new ResponseEntity<>("Group Joined!", HttpStatus.ACCEPTED);
    }

    // TODO:   Currently generating duplicate entries (One member getting added in one group multiple times creating false entries(NonUniqueResultException))


    // Get All Members
    @GetMapping("/groups/{id}/members")
    public ResponseEntity<List<GroupMemberDetailsResponse>> getAllGroupMembers(
            @PathVariable("id") int groupId ){

        List<GroupMemberDetailsResponse> allMembers = service.getAllGroupMembers(groupId);

        return new ResponseEntity<>(allMembers,HttpStatus.OK);
    }


    // Leave Group
    @DeleteMapping("/groups/{id}/leave")
    public ResponseEntity<JoinLeaveResponse> leaveGroup(
            @PathVariable("id") int groupId, @RequestBody JoinLeaveRequest request){

        JoinLeaveResponse response = service.leaveGroup(groupId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // Search By Keyword
    @GetMapping("/search")
    public ResponseEntity<List<StudyGroup> > search(@RequestParam String keyword){
        List<StudyGroup> groups = service.searchGroups(keyword);
        System.out.println("Searching with " + keyword);
        return new ResponseEntity<>(groups, HttpStatus.FOUND);
    }

    @GetMapping("/groups/{id}/join-date")
    public ResponseEntity<List<GroupMemberDetailsResponse>> getMemberByDate(
            @PathVariable("id") int groupId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate,
            Pageable pageable){

       List<GroupMemberDetailsResponse> members = service.filterMemberByDate(groupId, startDate, endDate, pageable);
        return new ResponseEntity<>(members, HttpStatus.OK);
    }



}
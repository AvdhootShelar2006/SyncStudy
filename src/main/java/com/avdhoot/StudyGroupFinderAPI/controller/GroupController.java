package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.model.dto.group_member_dto.GroupMemberDetailsResponse;
import com.avdhoot.StudyGroupFinderAPI.model.dto.group_member_dto.JoinLeaveRequest;
import com.avdhoot.StudyGroupFinderAPI.model.dto.group_member_dto.JoinLeaveResponse;
import com.avdhoot.StudyGroupFinderAPI.model.entities.Member;
import com.avdhoot.StudyGroupFinderAPI.model.entities.StudyGroup;
import com.avdhoot.StudyGroupFinderAPI.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
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
    public ResponseEntity<JoinLeaveResponse> joinGroup(
            @PathVariable("id") int groupId, @RequestBody JoinLeaveRequest request){

        JoinLeaveResponse response = service.joinGroup(groupId, request);

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }


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
}
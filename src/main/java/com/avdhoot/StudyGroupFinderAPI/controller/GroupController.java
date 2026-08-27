package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.CreateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.GroupResponseDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.UpdateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto.*;
import com.avdhoot.StudyGroupFinderAPI.entity.StudyGroup;
import com.avdhoot.StudyGroupFinderAPI.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService service;

    // Create Group
    @PostMapping("/groups")
    public ResponseEntity<GroupResponseDto> createGroup(
            @Valid @RequestBody CreateGroupRequestDto requestDto
    ){
            GroupResponseDto groupResponseDto = service.createGroup(requestDto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(groupResponseDto);
    }

    @PatchMapping("/groups/{groupId}")
    public ResponseEntity<GroupResponseDto> updateGroup(
            @PathVariable("groupId") Integer groupId,
            @Valid @RequestBody UpdateGroupRequestDto updateGroup
    ){
        return ResponseEntity.ok(service.updateGroup(groupId, updateGroup));
    }

    @GetMapping("/groups")
    public ResponseEntity<List<GroupResponseDto>> getAllGroups(){
        return new ResponseEntity<>(service.getAllGroups(), HttpStatus.OK);
    }

    @GetMapping("/groups/{groupId}")
    public ResponseEntity<GroupResponseDto> getGroupById(@PathVariable("groupId") Integer groupId){
        return ResponseEntity.ok(service.getGroupById(groupId));
    }

    @PostMapping("/groups/{groupId}/join")
    public ResponseEntity <JoinGroupResponse> joinGroup(
            @PathVariable("groupId") Integer groupId,
            @RequestBody JoinGroupRequest request
    ){
        return new ResponseEntity<>(service.joinGroup(groupId, request), HttpStatus.CREATED);
    }

    // Get All Members
    @GetMapping("/groups/{groupId}/members")
    public ResponseEntity<List<GroupMemberDetailsResponse>> getAllGroupMembers(
            @PathVariable("groupId") int groupId ){
        List<GroupMemberDetailsResponse> allMembers = service.getAllGroupMembers(groupId);
        return new ResponseEntity<>(allMembers,HttpStatus.OK);
    }

    // Leave Group
    @DeleteMapping("/groups/{groupId}/leave")
    public ResponseEntity<LeaveResponseDto> leaveGroup(
            @PathVariable("groupId") Integer groupId,
            @RequestBody LeaveRequestDto request
    ){
        LeaveResponseDto response = service.leaveGroup(groupId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Search By Keyword
    @GetMapping("/groups/search")
    public ResponseEntity<List<StudyGroup>> search(@RequestParam String keyword){
        List<StudyGroup> groups = service.searchGroups(keyword);
        System.out.println("Searching with " + keyword);
        return new ResponseEntity<>(groups, HttpStatus.FOUND);
    }
}
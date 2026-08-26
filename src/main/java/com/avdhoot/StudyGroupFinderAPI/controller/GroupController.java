package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.CreateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.GroupResponseDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto.GroupMemberDetailsResponse;
import com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto.JoinLeaveRequest;
import com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto.JoinLeaveResponse;
import com.avdhoot.StudyGroupFinderAPI.entity.StudyGroup;
import com.avdhoot.StudyGroupFinderAPI.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("groupmate")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService service;

    // Create Group
    @PostMapping("/create")
    public ResponseEntity<GroupResponseDto> createGroup(@RequestBody CreateGroupRequestDto requestDto){


        try{
            GroupResponseDto group = service.createGroup(requestDto);
            return new ResponseEntity<>(group, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateGroup(
            @RequestParam("groupId") int groupId,
            @RequestBody StudyGroup group
    ){
        StudyGroup studyGroup = null;
        try{
            group.setId(groupId);
            studyGroup =  service.updateGroup(groupId, group);
            return new ResponseEntity<>(studyGroup, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/groups")
    public ResponseEntity<List<GroupResponseDto>> getAllGroups(){
        return new ResponseEntity<>(service.getAllGroups(), HttpStatus.OK);
    }

    @GetMapping("/group")
    public ResponseEntity<GroupResponseDto> getGroupById(@RequestParam("groupId") int groupId){
        if(groupId > 0){
            return new ResponseEntity<>(service.getGroupById(groupId), HttpStatus.FOUND);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/join")
    public ResponseEntity<List<JoinLeaveResponse>> joinGroup(
            @RequestParam("groupId") int groupId,
            @RequestBody List<JoinLeaveRequest> request
    ){
        return new ResponseEntity<>( service.joinGroup(groupId, request), HttpStatus.ACCEPTED);
    }
    // TODO:   Currently generating duplicate entries (One member getting added in one group multiple times creating false entries(NonUniqueResultException))

    // Get All Members
    @GetMapping("/members")
    public ResponseEntity<List<GroupMemberDetailsResponse>> getAllGroupMembers(
            @RequestParam("groupId") int groupId ){
        List<GroupMemberDetailsResponse> allMembers = service.getAllGroupMembers(groupId);
        return new ResponseEntity<>(allMembers,HttpStatus.OK);
    }

    // Leave Group
    @DeleteMapping("/{id}/leave")
    public ResponseEntity<JoinLeaveResponse> leaveGroup(
            @PathVariable("id") int groupId, @RequestBody JoinLeaveRequest request){
        JoinLeaveResponse response = service.leaveGroup(groupId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Search By Keyword
    @GetMapping("/search")
    public ResponseEntity<List<StudyGroup>> search(@RequestParam String keyword){
        List<StudyGroup> groups = service.searchGroups(keyword);
        System.out.println("Searching with " + keyword);
        return new ResponseEntity<>(groups, HttpStatus.FOUND);
    }


    /*
    @GetMapping("/groups/{id}/join-date")
    public ResponseEntity<List<GroupMemberDetailsResponse>> getMemberByDate(
            @PathVariable("id") int groupId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate,
            Pageable pageable){

       List<GroupMemberDetailsResponse> members = service.filterMemberByDate(groupId, startDate, endDate, pageable);
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

     */

}
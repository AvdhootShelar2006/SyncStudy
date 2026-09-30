package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.CreateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.GroupResponseDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.UpdateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto.*;
import com.avdhoot.StudyGroupFinderAPI.entity.CustomUserDetails;
import com.avdhoot.StudyGroupFinderAPI.entity.Group;
import com.avdhoot.StudyGroupFinderAPI.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
//@RequiredArgsConstructor
public class GroupController {

    private  GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }
    /*
        Create Group
     */
    @PostMapping("/groups")
    public ResponseEntity<GroupResponseDto> createGroup(
            @Valid @RequestBody CreateGroupRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){

            GroupResponseDto groupResponseDto = groupService.createGroup(requestDto, userDetails.getUser());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(groupResponseDto);
    }


    /*
        Update Groups
     */
    @PatchMapping("/groups/{groupId}")
    public ResponseEntity<GroupResponseDto> updateGroup(
            @PathVariable("groupId") Integer groupId,
            @Valid @RequestBody UpdateGroupRequestDto updateGroup
    ){
        return ResponseEntity.ok(groupService.updateGroup(groupId, updateGroup));
    }


    /*
        Get All the Groups
    */
    @GetMapping("/groups")
    public ResponseEntity<Page<GroupResponseDto>> getAllGroups(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<GroupResponseDto> response = groupService.getAllGroups(pageable);

        return ResponseEntity.ok(response);
    }


    /*
        Get Group By Id
     */
    @GetMapping("/groups/{groupId}")
    public ResponseEntity<GroupResponseDto> getGroupById(@PathVariable("groupId") Integer groupId){
        return ResponseEntity.ok(groupService.getGroupById(groupId));
    }

    /*
        Join Group
     */
    @PostMapping("/groups/{groupId}/join")
    public ResponseEntity <JoinGroupResponse> joinGroup(
            @PathVariable("groupId") Integer groupId,
           @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        JoinGroupResponse response = groupService.joinGroup(groupId, userDetails.getUser());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /*
        Get All Members
     */
    @GetMapping("/groups/{groupId}/members")
    public ResponseEntity<Page<GroupMemberDetailsResponse>> getAllGroupMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable("groupId") int groupId ){

        Pageable pageable = PageRequest.of(page, size);

        Page<GroupMemberDetailsResponse> allMembers = groupService.getAllGroupMembers(pageable,groupId);

        return new ResponseEntity<>(allMembers,HttpStatus.OK);
    }

    /*
        Leave Group
     */
    @DeleteMapping("/groups/{groupId}/leave")
    public ResponseEntity<LeaveResponseDto> leaveGroup(
            @PathVariable("groupId") Integer groupId,
            @RequestBody LeaveRequestDto request
    ){
        LeaveResponseDto response = groupService.leaveGroup(groupId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*
        Search By Keyword
     */
    @GetMapping("/groups/search")
    public ResponseEntity<List<Group>> search(@RequestParam String keyword){
        List<Group> groups = groupService.searchGroups(keyword);
        System.out.println("Searching with " + keyword);
        return new ResponseEntity<>(groups, HttpStatus.FOUND);
    }
}
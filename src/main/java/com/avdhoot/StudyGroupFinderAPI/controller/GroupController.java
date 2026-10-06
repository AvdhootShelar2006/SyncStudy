package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.CreateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.GroupResponseDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.UpdateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto.*;
import com.avdhoot.StudyGroupFinderAPI.entity.CustomUserDetails;
import com.avdhoot.StudyGroupFinderAPI.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api")
public class GroupController {

    private final GroupService groupService;

    // TODO: Fix every dto so that it returns the specific id and fix enable as to expose int dto for group
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }
    /**
      *  Create Group
     */
    @PostMapping("/createGroup")
    public ResponseEntity<GroupResponseDto> createGroup(
            @Valid @RequestBody CreateGroupRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){

            GroupResponseDto groupResponseDto = groupService.createGroup(requestDto, userDetails.getUser());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(groupResponseDto);
    }

    /**
     *  Update Groups
     */
    @PreAuthorize("@groupSecurityConfig.hasRole(authentication, #groupId, T(com.avdhoot.StudyGroupFinderAPI.enums.GroupRole).OWNER)")
    @PatchMapping("/updateGroup/{groupId}")
    public ResponseEntity<GroupResponseDto> updateGroup(
            @PathVariable("groupId") Integer groupId,
            @Valid @RequestBody UpdateGroupRequestDto updateGroup
    ){
        return ResponseEntity.ok(groupService.updateGroup(groupId, updateGroup));
    }


    /**
     *  Get All the Groups
    */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/groups")
    public ResponseEntity<Page<GroupResponseDto>> getAllGroups(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<GroupResponseDto> response = groupService.getAllGroups(pageable);

        return ResponseEntity.ok(response);
    }


    /**
     * Get Group By ID
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group/{groupId}")
    public ResponseEntity<GroupResponseDto> getGroupById(@PathVariable("groupId") Integer groupId){
        return ResponseEntity.ok(groupService.getGroupById(groupId));
    }

    /**
     *  Join Group
     */
    // TODO: Fix the join group for max member check and create and exception custom
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/join/{groupId}")
    public ResponseEntity <JoinGroupResponse> joinGroup(
            @PathVariable("groupId") Integer groupId,
           @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        JoinGroupResponse response = groupService.joinGroup(groupId, userDetails.getUser());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     *  Get All Members in a Group
     */
    @PreAuthorize("@groupSecurityConfig.hasRole(authentication, #groupId, T(com.avdhoot.StudyGroupFinderAPI.enums.GroupRole).OWNER)")
    @GetMapping("/groupMembers/{groupId}/members")
    public ResponseEntity<Page<GroupMemberDetailsResponse>> getAllGroupMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable("groupId") int groupId ){

        Pageable pageable = PageRequest.of(page, size);

        Page<GroupMemberDetailsResponse> allMembers = groupService.getAllGroupMembers(pageable,groupId);

        return new ResponseEntity<>(allMembers,HttpStatus.OK);
    }

    /**
     * Leave Group
     */
    @PreAuthorize("@groupSecurityConfig.hasRole(authentication, #groupId, T(com.avdhoot.StudyGroupFinderAPI.enums.GroupRole).MEMBER)")
    @DeleteMapping("/groups/{groupId}/leave")
    public ResponseEntity<LeaveResponseDto> leaveGroup(
            @PathVariable("groupId") Integer groupId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        LeaveResponseDto response = groupService.leaveGroup(groupId, userDetails.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Remove member only by Group OWNER
     */
    @PreAuthorize("@groupSecurityConfig.canManageMember(authentication, #groupId, #userId)")
    @DeleteMapping("/groupMembers/{groupId}/members/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable int groupId,
            @PathVariable int userId
    ) {
        groupService.removeMember(groupId, userId);
        return ResponseEntity.noContent().build();
    }

    /*
        Search By Keyword
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/groups/search")
    public ResponseEntity<Page<GroupResponseDto>> search(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String keyword){
        int safeSize = Math.min(size, 50);
        Pageable pageable = PageRequest.of(page, safeSize,Sort.by("groupName"));
        Page<GroupResponseDto> responseDto = groupService.searchGroups(pageable, keyword);
        System.out.println("Searching with " + keyword);
        return ResponseEntity.ok(responseDto);
    }


    /*
       Get All Groups the user is part of
    */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("communities")
    public ResponseEntity<Page<GroupResponseDto>> getAllGroupsUserIsJoinedIn(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        Pageable pageable = PageRequest.of(page, size);

        Page<GroupResponseDto> createGroupRequestDto = groupService.getAllGroupsUserIsJoinedIn(pageable, userDetails);
        return ResponseEntity.ok(createGroupRequestDto);
    }

    @PreAuthorize("@groupSecurityConfig.hasRole(authentication, #groupId, T(com.avdhoot.StudyGroupFinderAPI.enums.GroupRole).OWNER)")
    @DeleteMapping("/group/{groupId}/delete")
    public ResponseEntity<Void> deleteGroup(@PathVariable("groupId") Integer groupId){
        groupService.deleteGroup(groupId);
        return ResponseEntity.noContent().build();
    }
}
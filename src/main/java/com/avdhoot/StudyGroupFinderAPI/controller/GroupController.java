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

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }
    /*
        Create Group
     */
    @PostMapping("/createGroup")
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
    @PreAuthorize("@groupSecurityConfig.hasRole(authentication, #groupId, T(com.avdhoot.StudyGroupFinderAPI.enums.GroupRole).OWNER)")
    @PatchMapping("/updateGroup/{groupId}")
    public ResponseEntity<GroupResponseDto> updateGroup(
            @PathVariable("groupId") Integer groupId,
            @Valid @RequestBody UpdateGroupRequestDto updateGroup
    ){
        return ResponseEntity.ok(groupService.updateGroup(groupId, updateGroup));
    }


    /*
        Get All the Groups
    */
    @PreAuthorize(("hasRole('USER')"))
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
    @PreAuthorize(("hasRole('USER')"))
    @GetMapping("/group/{groupId}")
    public ResponseEntity<GroupResponseDto> getGroupById(@PathVariable("groupId") Integer groupId){
        return ResponseEntity.ok(groupService.getGroupById(groupId));
    }

    /*
        Join Group
     */
    @PreAuthorize(("hasRole('USER')"))
    @PostMapping("/join/{groupId}")
    public ResponseEntity <JoinGroupResponse> joinGroup(
            @PathVariable("groupId") Integer groupId,
           @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        JoinGroupResponse response = groupService.joinGroup(groupId, userDetails.getUser());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /*
        Get All Members in a Group
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

    /*
        Leave Group
     */
    @PreAuthorize("@groupSecurityConfig.isGroupMember(authentication, #groupId)")
    @DeleteMapping("/groups/{groupId}/leave")
    public ResponseEntity<LeaveResponseDto> leaveGroup(
            @PathVariable("groupId") Integer groupId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        LeaveResponseDto response = groupService.leaveGroup(groupId, userDetails.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*
        Search By Keyword
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/groups/search")
    public ResponseEntity<Page<GroupResponseDto>> search(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String keyword){
        int safeSize = Math.min(size, 50);
        Pageable pageable = PageRequest.of(page, safeSize,Sort.by("groupName"));
        Page<GroupResponseDto> responseDtos = groupService.searchGroups(pageable, keyword);
        System.out.println("Searching with " + keyword);
        return ResponseEntity.ok(responseDtos);
    }

    /*
        Get All Groups the user is part of
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/groups/groupmemberships")
    public ResponseEntity<Page<GroupResponseDto>> getAllGroupsTheUserIsJoinedIn(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<GroupResponseDto> responseDtos = groupService.getAllGroupsTheUserIsJoinedIn(pageable, userDetails.getId());

        return ResponseEntity.ok(responseDtos);
    }
}
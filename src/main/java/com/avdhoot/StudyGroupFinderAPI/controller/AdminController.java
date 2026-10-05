package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.GroupResponseDto;
import com.avdhoot.StudyGroupFinderAPI.dto.userDto.CreateUserDetailResponse;
import com.avdhoot.StudyGroupFinderAPI.entity.CustomUserDetails;
import com.avdhoot.StudyGroupFinderAPI.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @PatchMapping("userstatus/{userId}")
    public ResponseEntity<String> disableOrEnableUser(@PathVariable("userId") int userId, @RequestParam boolean enabled) {
        String userStatus = adminService.disableOrEnableUser( userId, enabled);
        return new ResponseEntity<>(userStatus, HttpStatus.OK);
    }

    @PatchMapping("groupstatus/{groupId}")
    public ResponseEntity<String> disableOrEnableGroup(@PathVariable("groupId") int groupId, @RequestParam boolean enabled) {
        String groupStatus = adminService.disableOrEnableGroup( groupId, enabled);
        return new ResponseEntity<>(groupStatus, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Page<CreateUserDetailResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);

        Page<CreateUserDetailResponse> createMemberDetailResponses = adminService.getAllUsers(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(createMemberDetailResponses);
    }

    @GetMapping("/suspendedGroups")
    public ResponseEntity<Page<GroupResponseDto>> getAllSuspendedGroups(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<GroupResponseDto> response = adminService.getAllSuspendedGroups(pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/suspendedUser")
    public ResponseEntity<Page<CreateUserDetailResponse>> getAllSuspendedUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);

        Page<CreateUserDetailResponse> createMemberDetailResponses = adminService.getAllSuspendedUsers(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(createMemberDetailResponses);
    }
}

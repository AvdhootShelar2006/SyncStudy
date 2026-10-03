package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.dto.reportDto.ReportRequest;
import com.avdhoot.StudyGroupFinderAPI.dto.reportDto.ReportStatusRequest;
import com.avdhoot.StudyGroupFinderAPI.dto.reportDto.ReportStatusResponse;
import com.avdhoot.StudyGroupFinderAPI.entity.CustomUserDetails;
import com.avdhoot.StudyGroupFinderAPI.service.ReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    private final ReportService service;

    @PostMapping("/groups/{groupId}/report")
    public ResponseEntity<?> createReport(
            @PathVariable Integer groupId,
            @Valid @RequestBody ReportRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        service.createReport(groupId, request, userDetails.getId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PreAuthorize("@groupSecurityConfig.hasRole(authentication, #groupId, T(com.avdhoot.StudyGroupFinderAPI.enums.GroupRole).OWNER)")
    @GetMapping("/groups/{groupId}/report")
    public ResponseEntity<Page<ReportStatusResponse>> getAllReport(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable("groupId") Integer groupId
    ){

        Pageable pageable = PageRequest.of(page, size);

        Page<ReportStatusResponse> reportStatusResponses = service.getAllReport(groupId, pageable);
        return new ResponseEntity<>(reportStatusResponses, HttpStatus.FOUND);
    }


    @PreAuthorize("@groupSecurityConfig.hasRole(authentication, #groupId, T(com.avdhoot.StudyGroupFinderAPI.enums.GroupRole).OWNER)")
    @PatchMapping("/group/{groupId}/reports/{reportId}/status")
    public ResponseEntity<ReportStatusResponse> updateReportStatus(
            @PathVariable int groupId,
            @PathVariable("reportId") int reportId,
            @RequestBody ReportStatusRequest reportStatusRequest
    ){
        ReportStatusResponse statusResponse = service.updateReportStatus(groupId,reportId, reportStatusRequest);
        return ResponseEntity.ok(statusResponse);
    }
}

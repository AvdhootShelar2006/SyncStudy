package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.dto.reportDto.ReportRequest;
import com.avdhoot.StudyGroupFinderAPI.dto.reportDto.ReportStatusRequest;
import com.avdhoot.StudyGroupFinderAPI.dto.reportDto.ReportStatusResponse;
import com.avdhoot.StudyGroupFinderAPI.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService service;

    @PostMapping("/groups/{groupId}/report")
    public ResponseEntity<?> createReport(
            @PathVariable Integer groupId,
            @Valid @RequestBody ReportRequest request
    ) {
        service.createReport(groupId, request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

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

    @PatchMapping("/reports/{reportId}/status")
    public ResponseEntity<?> updateReportStatus(
            @PathVariable("reportId") Integer reportId,
            @RequestBody ReportStatusRequest reportStatusRequest
    ){
        service.updateReportStatus(reportId, reportStatusRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

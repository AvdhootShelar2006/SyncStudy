package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.model.dto.report_dto.ReportRequest;
import com.avdhoot.StudyGroupFinderAPI.model.dto.report_dto.ReportStatusRequest;
import com.avdhoot.StudyGroupFinderAPI.model.dto.report_dto.ReportStatusResponse;
import com.avdhoot.StudyGroupFinderAPI.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("groupmate")
public class ReportController {
    @Autowired
    private ReportService service;

    @PostMapping("/group/{id}/report")
    public ResponseEntity<?> groupReport(@PathVariable("id") int groupId, @RequestBody ReportRequest request){
//        List<ReportStatusResponse> responses = service.groupReport(groupId, request);;

        service.groupReport(groupId, request);
        return new ResponseEntity<>( HttpStatus.CREATED);
    }

    @GetMapping("/groups/{id}/report")
    public ResponseEntity<List<ReportStatusResponse>> getReport(@PathVariable("id") int groupId){
        List<ReportStatusResponse> reportStatusResponses = service.getReport(groupId);
        return new ResponseEntity<>(reportStatusResponses, HttpStatus.FOUND);
    }

    @PatchMapping("/reports/{id}/status")
    public ResponseEntity<?> updateReportStatus(@PathVariable("id") int reportId, @RequestBody ReportStatusRequest reportStatusRequest){
        try{
             service.updateReportStatus(reportId,reportStatusRequest);
            return new ResponseEntity<>( HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
    }
}

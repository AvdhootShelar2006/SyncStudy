package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.entity.Report;
import com.avdhoot.StudyGroupFinderAPI.dto.reportDto.ReportRequest;
import com.avdhoot.StudyGroupFinderAPI.dto.reportDto.ReportStatusRequest;
import com.avdhoot.StudyGroupFinderAPI.dto.reportDto.ReportStatusResponse;
import com.avdhoot.StudyGroupFinderAPI.entity.User;
import com.avdhoot.StudyGroupFinderAPI.entity.Group;
import com.avdhoot.StudyGroupFinderAPI.enums.ReportStatus;
import com.avdhoot.StudyGroupFinderAPI.exception.EntityAndRelationshipsFinder;
import com.avdhoot.StudyGroupFinderAPI.exception.ResourceNotFoundException;
import com.avdhoot.StudyGroupFinderAPI.mapper.ReportMapper;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupMembershipRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final GroupMembershipRepository groupMembershipRepository;
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final EntityAndRelationshipsFinder entityAndRelationshipsFinder;

    public void createReport(int groupId, ReportRequest request) {
        User reportedBy = entityAndRelationshipsFinder.getUserOrThrow(request.reportedBy());
        User targetUser = entityAndRelationshipsFinder.getUserOrThrow(request.targetMember());
        Group group = entityAndRelationshipsFinder.getGroupOrThrow(groupId);

        if(!groupMembershipRepository.existsByGroupAndUser(group, reportedBy)){
            throw new ResourceNotFoundException(reportedBy.getName() + " is not the part of the group");
        }
        if(!groupMembershipRepository.existsByGroupAndUser(group, targetUser)){
            throw new ResourceNotFoundException(targetUser.getName() + " is not the part of the group");
        }

        Report report = Report
                .builder()
                .reportedBy(reportedBy)
                .targetUser(targetUser)
                .reason(request.reason())
                .status(ReportStatus.PENDING)
                .reportedTime(LocalDateTime.now())
                .targetGroupId(group)
                .build();

        reportRepository.save(report);
    }

    public Page<ReportStatusResponse> getAllReport(int groupId, Pageable pageable) {
        Page<Report> reports = reportRepository.findByTargetGroupId_Id(groupId, pageable);

        return reports.map(reportMapper::toReportStatusResponse);
    }

    public void updateReportStatus(int reportId, ReportStatusRequest reportStatusRequest) {
        Report report = reportRepository.findById(reportId).orElseThrow(()->new ResourceNotFoundException("Report with " + reportId + " not found"));

        if(reportStatusRequest.status() != null){
            report.setStatus(reportStatusRequest.status());
        }
        reportRepository.save(report);
    }
}

package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.entity.Report;
import com.avdhoot.StudyGroupFinderAPI.dto.reportDto.ReportRequest;
import com.avdhoot.StudyGroupFinderAPI.dto.reportDto.ReportStatusRequest;
import com.avdhoot.StudyGroupFinderAPI.dto.reportDto.ReportStatusResponse;
import com.avdhoot.StudyGroupFinderAPI.entity.Member;
import com.avdhoot.StudyGroupFinderAPI.entity.StudyGroup;
import com.avdhoot.StudyGroupFinderAPI.enums.ReportStatus;
import com.avdhoot.StudyGroupFinderAPI.exception.EntityAndRelationshipsFinder;
import com.avdhoot.StudyGroupFinderAPI.exception.ResourceNotFoundException;
import com.avdhoot.StudyGroupFinderAPI.mapper.ReportMapper;
import com.avdhoot.StudyGroupFinderAPI.repository.MemberRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupMembershipRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.ReportRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final GroupMembershipRepository groupMembershipRepository;
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final EntityAndRelationshipsFinder entityAndRelationshipsFinder;

    public void createReport(int groupId, ReportRequest request) {
        Member reportedBy = entityAndRelationshipsFinder.getMemberOrThrow(request.reportedBy());
        Member targetMember = entityAndRelationshipsFinder.getMemberOrThrow(request.targetMember());
        StudyGroup group = entityAndRelationshipsFinder.getGroupOrThrow(groupId);

        if(!groupMembershipRepository.existsByGroupAndMember(group, reportedBy)){
            throw new ResourceNotFoundException(reportedBy.getName() + " is not the part of the group");
        }
        if(!groupMembershipRepository.existsByGroupAndMember(group, targetMember)){
            throw new ResourceNotFoundException(targetMember.getName() + " is not the part of the group");
        }

        Report report = Report
                .builder()
                .reportedBy(reportedBy)
                .targetMember(targetMember)
                .reason(request.reason())
                .status(ReportStatus.PENDING)
                .reportedTime(LocalDateTime.now())
                .targetGroupId(group)
                .build();

        reportRepository.save(report);
    }

    public List<ReportStatusResponse> getAllReport(int groupId) {
        List<Report> reports = reportRepository.findByTargetGroupId_Id(groupId);
        return reportMapper.toListReportStatusResponse(reports);
    }

    public void updateReportStatus(int reportId, ReportStatusRequest reportStatusRequest) {
        Report report = reportRepository.findById(reportId).orElseThrow(()->new ResourceNotFoundException("Report with " + reportId + " not found"));

        if(reportStatusRequest.status() != null){
            report.setStatus(reportStatusRequest.status());
        }
        reportRepository.save(report);
    }
}

package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.model.Report;
import com.avdhoot.StudyGroupFinderAPI.model.dto.report_dto.ReportRequest;
import com.avdhoot.StudyGroupFinderAPI.model.dto.report_dto.ReportStatusRequest;
import com.avdhoot.StudyGroupFinderAPI.model.dto.report_dto.ReportStatusResponse;
import com.avdhoot.StudyGroupFinderAPI.model.entities.Member;
import com.avdhoot.StudyGroupFinderAPI.model.entities.StudyGroup;
import com.avdhoot.StudyGroupFinderAPI.model.entities.enums.ReportStatus;
import com.avdhoot.StudyGroupFinderAPI.repository.MemberRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupMembershipRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.ReportRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GroupMembershipRepository groupMembershipRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private GroupRepository groupRepository;

    public void groupReport(int groupId, ReportRequest request) {
        Member reportedBy = memberRepository.findById(request.reportedBy())
                .orElseThrow(() -> new RuntimeException("Reporter does not exist"));

        Member targetMember = memberRepository.findById(request.targetMember())
                .orElseThrow(() -> new RuntimeException("Target member does not exist"));
        StudyGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group does not exist"));

//        if(!groupMembershipRepository.existsByGroup_IdAndMember_Id(groupId, request.reportedBy())){
//            throw new RuntimeException("You are not a member of the group!");
//        }
//
//        if(!groupMembershipRepository.existsByGroup_IdAndMember_Id(groupId, request.targetMember())){
//            throw new RuntimeException("The reported user is not a member of the group!");
//        }

        if(!groupMembershipRepository.existsByGroupAndMember(group, reportedBy)){
            throw new RuntimeException("You are not a member of the group!");
        }

        if(!groupMembershipRepository.existsByGroupAndMember(group, targetMember)){
            throw new RuntimeException("The reported user is not a member of the group!");
        }

        Report report = new Report();

        report.setReportedBy(reportedBy);
        report.setTargetMemberId(targetMember);
        report.setReason(request.reason());
        report.setStatus(ReportStatus.PENDING);
        report.setReportedTime(LocalDateTime.now());
        report.setTargetGroupId(group);

        reportRepository.save(report);
    }


    public List<ReportStatusResponse> getReport(int groupId) {
        List<Report> reports = reportRepository.findByTargetGroupId_Id(groupId);

        List<ReportStatusResponse> responses = new ArrayList<>();

        for(Report report : reports){
            ReportStatusResponse response = new ReportStatusResponse(
                    report.getTargetMemberId().getId(),
                    report.getReason(),
                    report.getStatus(),
                    report.getReportedTime()
            );
            responses.add(response);
        }
        return responses;
    }


    public void updateReportStatus(int reportId, ReportStatusRequest reportStatusRequest) {
        Report report = reportRepository.findById(reportId).orElseThrow(()->new RuntimeException("Failed Report Status Upadate"));

        if(reportStatusRequest != null){
            report.setStatus(reportStatusRequest.status());
        }
        reportRepository.save(report);
    }
}

package com.avdhoot.StudyGroupFinderAPI.model.dto.report_dto;

public record ReportRequest(
        int reportedBy,
        int targetMember,
        String reason
) {
}

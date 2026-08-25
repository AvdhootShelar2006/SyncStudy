package com.avdhoot.StudyGroupFinderAPI.model.dto.reportDto;

public record ReportRequest(
        int reportedBy,
        int targetMember,
        String reason
) {
}

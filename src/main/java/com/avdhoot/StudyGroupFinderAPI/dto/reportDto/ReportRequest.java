package com.avdhoot.StudyGroupFinderAPI.dto.reportDto;

public record ReportRequest(
        int reportedBy,
        int targetMember,
        String reason
) {
}

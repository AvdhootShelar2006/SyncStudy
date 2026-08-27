package com.avdhoot.StudyGroupFinderAPI.dto.reportDto;

public record ReportRequest(
        Integer reportedBy,
        Integer targetMember,
        String reason
) {
}

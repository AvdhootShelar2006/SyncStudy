package com.avdhoot.StudyGroupFinderAPI.dto.reportDto;

import com.avdhoot.StudyGroupFinderAPI.enums.ReportStatus;

import java.time.LocalDateTime;

public record ReportStatusResponse(
        Integer targetMemberId,
        String reason,
        ReportStatus status,
        LocalDateTime reportedTime
) {
}

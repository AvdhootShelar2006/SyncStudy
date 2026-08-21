package com.avdhoot.StudyGroupFinderAPI.model.dto.report_dto;

import com.avdhoot.StudyGroupFinderAPI.model.entities.enums.ReportStatus;

import java.time.LocalDateTime;

public record ReportStatusResponse(
        Integer targetMemberId,
        String reason,
        ReportStatus status,
        LocalDateTime reportedTime
) {
}

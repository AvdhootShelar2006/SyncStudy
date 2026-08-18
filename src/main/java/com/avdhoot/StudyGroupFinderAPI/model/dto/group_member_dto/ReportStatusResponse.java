package com.avdhoot.StudyGroupFinderAPI.model.dto.group_member_dto;

import com.avdhoot.StudyGroupFinderAPI.model.entities.enums.ReportStatus;

import java.time.LocalDateTime;

public record ReportStatusResponse(
        String groupName,
        String targetMemberId,
        String reason,
        ReportStatus status,
        LocalDateTime reportedTime
) {
}

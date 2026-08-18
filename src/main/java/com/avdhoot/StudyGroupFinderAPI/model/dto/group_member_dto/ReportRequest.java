package com.avdhoot.StudyGroupFinderAPI.model.dto.group_member_dto;

public record ReportRequest(
        int reportedBy,
        int targetMember,
        String reason
) {
}

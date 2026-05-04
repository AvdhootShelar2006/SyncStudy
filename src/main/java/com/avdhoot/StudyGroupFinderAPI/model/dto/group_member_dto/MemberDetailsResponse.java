package com.avdhoot.StudyGroupFinderAPI.model.dto.group_member_dto;

import java.time.LocalDate;

public record MemberDetailsResponse(
        String name,
        String email,
        LocalDate createdAt
) {
}

package com.avdhoot.StudyGroupFinderAPI.dto.memberDto;

import java.time.LocalDate;

public record CreateMemberDetailResponse(
        String name,
        String email,
        LocalDate createdAt
) {
}

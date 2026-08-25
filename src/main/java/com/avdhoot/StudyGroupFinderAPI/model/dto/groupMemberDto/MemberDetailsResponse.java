package com.avdhoot.StudyGroupFinderAPI.model.dto.groupMemberDto;

import java.time.LocalDate;

public record MemberDetailsResponse(
        String name,
        String email,
        LocalDate createdAt
) {
}

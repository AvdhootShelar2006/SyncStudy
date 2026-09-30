package com.avdhoot.StudyGroupFinderAPI.dto.userDto;

import java.time.LocalDate;

public record CreateUserDetailResponse(
        String name,
        String email,
        String username,
        LocalDate createdAt
) {
}

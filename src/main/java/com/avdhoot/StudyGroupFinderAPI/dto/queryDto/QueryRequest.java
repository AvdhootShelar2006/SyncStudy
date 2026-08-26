package com.avdhoot.StudyGroupFinderAPI.dto.queryDto;

public record QueryRequest(
        String title,
        String description,
        Integer memberId
) {
}

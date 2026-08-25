package com.avdhoot.StudyGroupFinderAPI.model.dto.queryDto;

public record QueryRequest(
        String title,
        String description,
        Integer memberId
) {
}

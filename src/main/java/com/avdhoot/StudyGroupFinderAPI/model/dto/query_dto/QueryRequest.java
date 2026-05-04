package com.avdhoot.StudyGroupFinderAPI.model.dto.query_dto;

public record QueryRequest(
        String title,
        String description,
        Integer memberId
) {
}

package com.avdhoot.StudyGroupFinderAPI.model.dto.query_dto;

public record GroupQueryResponse(
         String title,
         String description,
         String postedByName,
         Integer groupId,
         boolean isResolved
) {
}

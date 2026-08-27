package com.avdhoot.StudyGroupFinderAPI.dto.queryDto;

public record CreateQueryResponse(
         String title,
         String description,
         String postedByName,
         Integer groupId,
         boolean isResolved
) {
}

package com.avdhoot.StudyGroupFinderAPI.dto.queryDto;

public record GroupQueryResponse(
         String title,
         String description,
         String postedByName,
         Integer groupId,
         boolean isResolved
) {
}

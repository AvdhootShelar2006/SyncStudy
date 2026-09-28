package com.avdhoot.StudyGroupFinderAPI.dto.groupDto;


public record GroupResponseDto(
        String name,
        String subject,
        String field,
        String description,
        Integer maxMembers,
        String tags
) {}

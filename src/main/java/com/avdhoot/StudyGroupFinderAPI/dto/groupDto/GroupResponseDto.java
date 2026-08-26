package com.avdhoot.StudyGroupFinderAPI.dto.groupDto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record GroupResponseDto(
        String name,
        String subject,
        String field,
        String description,
        Integer maxMembers,
        String tags
) {}

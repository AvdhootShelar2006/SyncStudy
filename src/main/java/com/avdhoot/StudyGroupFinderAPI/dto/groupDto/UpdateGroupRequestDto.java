package com.avdhoot.StudyGroupFinderAPI.dto.groupDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateGroupRequestDto(
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @Size(min = 2, max = 100, message = "Subject must be between 2 and 100 characters")
        String subject,

        String field,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @Min(value = 1, message = "Group must have at least 1 member")
        Integer maxMembers,

        String tags
) {
}

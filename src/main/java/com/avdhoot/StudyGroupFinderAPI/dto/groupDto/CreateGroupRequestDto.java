package com.avdhoot.StudyGroupFinderAPI.dto.groupDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateGroupRequestDto (
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @NotBlank(message = "Subject is required")
        @Size(min = 2, max = 100, message = "Subject must be between 2 and 100 characters")
        String subject,

        @NotBlank(message = "Field is required")
        String field,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @NotNull(message = "Maximum number of members is required")
        @Min(value = 1, message = "Group must have at least 1 user")
        Integer maxMembers,

        String tags
){}

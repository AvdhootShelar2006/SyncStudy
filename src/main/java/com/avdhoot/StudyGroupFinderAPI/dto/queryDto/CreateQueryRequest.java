package com.avdhoot.StudyGroupFinderAPI.dto.queryDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateQueryRequest(
        @NotBlank(message = "Title is required")
        @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
        String title,

        @NotBlank(message = "Description is required")
        @Size(min = 10, max = 8000, message = "Description must be between 10 and 8000 characters")
        String description
) {
}

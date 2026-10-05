package com.avdhoot.StudyGroupFinderAPI.dto.answerQuery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReplyRequest (

        @NotBlank(message = "Description is required")
        @Size(min = 10, max = 8000, message = "Description must be between 10 and 8000 characters")
        String content
) {
}

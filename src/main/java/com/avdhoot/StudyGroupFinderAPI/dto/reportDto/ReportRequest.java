package com.avdhoot.StudyGroupFinderAPI.dto.reportDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReportRequest(
        @NotNull(message = "Target member is required")
        Integer targetMember,

        @NotBlank(message = "Reason is required")
        String reason
) {
}

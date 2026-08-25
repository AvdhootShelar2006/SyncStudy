package com.avdhoot.StudyGroupFinderAPI.model.dto.reportDto;

import com.avdhoot.StudyGroupFinderAPI.model.enums.ReportStatus;

public record ReportStatusRequest(
        ReportStatus status
) {
}

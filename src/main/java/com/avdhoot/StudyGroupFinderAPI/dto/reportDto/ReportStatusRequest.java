package com.avdhoot.StudyGroupFinderAPI.dto.reportDto;

import com.avdhoot.StudyGroupFinderAPI.enums.ReportStatus;

public record ReportStatusRequest(
        ReportStatus status
) {
}

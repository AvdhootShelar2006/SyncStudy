package com.avdhoot.StudyGroupFinderAPI.model.dto.report_dto;

import com.avdhoot.StudyGroupFinderAPI.model.entities.enums.ReportStatus;

public record ReportStatusRequest(
        ReportStatus status
) {
}

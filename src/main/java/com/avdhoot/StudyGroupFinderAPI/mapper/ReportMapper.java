package com.avdhoot.StudyGroupFinderAPI.mapper;

import com.avdhoot.StudyGroupFinderAPI.dto.reportDto.ReportStatusResponse;
import com.avdhoot.StudyGroupFinderAPI.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(source = "id", target = "reportId")
    @Mapping(source = "targetUser.id", target = "targetMemberId")
    ReportStatusResponse toReportStatusResponse(Report report);

//    List<ReportStatusResponse> toListReportStatusResponse(List<Report> reports);
}

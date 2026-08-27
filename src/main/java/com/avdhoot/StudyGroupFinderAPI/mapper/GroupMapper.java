package com.avdhoot.StudyGroupFinderAPI.mapper;

import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.CreateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.GroupResponseDto;
import com.avdhoot.StudyGroupFinderAPI.entity.StudyGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    StudyGroup toEntity(CreateGroupRequestDto dto);

    GroupResponseDto toGroupResponseDto(StudyGroup studyGroup);

    List<GroupResponseDto> toGroupResponseDtoList(List<StudyGroup> studyGroup);
}

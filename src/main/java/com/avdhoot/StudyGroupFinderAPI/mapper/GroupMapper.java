package com.avdhoot.StudyGroupFinderAPI.mapper;

import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.CreateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.GroupResponseDto;
import com.avdhoot.StudyGroupFinderAPI.entity.Group;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    Group toEntity(CreateGroupRequestDto dto);

    GroupResponseDto toGroupResponseDto(Group group);

//    List<GroupResponseDto> toGroupResponseDtoList(List<Group> group);
}

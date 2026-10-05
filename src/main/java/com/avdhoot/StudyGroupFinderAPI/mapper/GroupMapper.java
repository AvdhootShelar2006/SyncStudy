package com.avdhoot.StudyGroupFinderAPI.mapper;

import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.CreateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.GroupResponseDto;
import com.avdhoot.StudyGroupFinderAPI.entity.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    @Mapping(source = "groupName", target = "groupName")
    Group toEntity(CreateGroupRequestDto dto);

    @Mapping(source = "groupName", target = "name")
    GroupResponseDto toGroupResponseDto(Group group);

//    List<GroupResponseDto> toGroupResponseDtoList(List<Group> group);
}

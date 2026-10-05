package com.avdhoot.StudyGroupFinderAPI.mapper;

import com.avdhoot.StudyGroupFinderAPI.dto.userDto.CreateUserDetailResponse;
import com.avdhoot.StudyGroupFinderAPI.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    @Mapping(source = "user.id", target = "userId")
    CreateUserDetailResponse toMemberResponseDto(User user);
}

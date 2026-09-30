package com.avdhoot.StudyGroupFinderAPI.mapper;

import com.avdhoot.StudyGroupFinderAPI.dto.memberDto.CreateUserDetailResponse;
import com.avdhoot.StudyGroupFinderAPI.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    CreateUserDetailResponse toMemberResponseDto(User user);
}

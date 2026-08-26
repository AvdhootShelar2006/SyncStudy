package com.avdhoot.StudyGroupFinderAPI.mapper;

import com.avdhoot.StudyGroupFinderAPI.dto.memberDto.CreateMemberDetailResponse;
import com.avdhoot.StudyGroupFinderAPI.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    CreateMemberDetailResponse toMemberResponseDto(Member member);
}

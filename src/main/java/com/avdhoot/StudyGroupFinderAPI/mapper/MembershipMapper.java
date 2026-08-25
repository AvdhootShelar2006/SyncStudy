package com.avdhoot.StudyGroupFinderAPI.mapper;

import com.avdhoot.StudyGroupFinderAPI.model.dto.groupMemberDto.GroupMemberDetailsResponse;
import com.avdhoot.StudyGroupFinderAPI.model.entity.GroupMembership;
import com.avdhoot.StudyGroupFinderAPI.model.entity.Member;
import com.avdhoot.StudyGroupFinderAPI.model.entity.StudyGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MembershipMapper {

    @Mapping(source = "member.name", target = "name")
    GroupMemberDetailsResponse toDto(GroupMembership groupMemberships);

    List<GroupMemberDetailsResponse> toDtoList(List<GroupMembership> groupMemberships);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "member", target = "member")
    @Mapping(source = "group", target = "group")
    @Mapping(target = "joinedAt",expression = "java(java.time.LocalDate.now())")
    GroupMembership createMembership(Member member, StudyGroup group);
}

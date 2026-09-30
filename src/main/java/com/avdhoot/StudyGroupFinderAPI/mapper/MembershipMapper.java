package com.avdhoot.StudyGroupFinderAPI.mapper;

import com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto.GroupMemberDetailsResponse;
import com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto.JoinGroupResponse;
import com.avdhoot.StudyGroupFinderAPI.entity.Group;
import com.avdhoot.StudyGroupFinderAPI.entity.GroupMembership;
import com.avdhoot.StudyGroupFinderAPI.entity.Roles;
import com.avdhoot.StudyGroupFinderAPI.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface MembershipMapper {

    @Mapping(source = "user.id", target = "memberId")
    @Mapping(source = "user.name", target = "name")
    GroupMemberDetailsResponse toGroupMemberDetailsResponse(GroupMembership groupMembership);


//    List<GroupMemberDetailsResponse> toGroupMemberDetailsResponses(List<GroupMembership> groupMemberships);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "user", target = "user")
    @Mapping(source = "group", target = "group")
    @Mapping(target = "joinedAt",expression = "java(java.time.LocalDate.now())")
    @Mapping(source = "roles", target = "roles")
    GroupMembership createMembership(User user, Group group, Set<Roles> roles);

    JoinGroupResponse toJoinGroupResponse(User user, Group group, GroupMembership groupMembership);
}

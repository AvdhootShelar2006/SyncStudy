package com.avdhoot.StudyGroupFinderAPI.mapper;

import com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto.GroupMemberDetailsResponse;
import com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto.JoinGroupResponse;
import com.avdhoot.StudyGroupFinderAPI.entity.Group;
import com.avdhoot.StudyGroupFinderAPI.entity.GroupMembership;
import com.avdhoot.StudyGroupFinderAPI.entity.Roles;
import com.avdhoot.StudyGroupFinderAPI.entity.User;
import com.avdhoot.StudyGroupFinderAPI.enums.GroupRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface MembershipMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    GroupMemberDetailsResponse toGroupMemberDetailsResponse(GroupMembership groupMembership);


//    List<GroupMemberDetailsResponse> toGroupMemberDetailsResponses(List<GroupMembership> groupMemberships);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "user", target = "user")
    @Mapping(source = "group", target = "group")
    @Mapping(target = "joinedAt",expression = "java(java.time.LocalDate.now())")
    @Mapping(source = "groupRole", target = "groupRole")
    GroupMembership createMembership(User user, Group group, GroupRole groupRole);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "groupMembership.joinedAt", target = "joinedAt")
    JoinGroupResponse toJoinGroupResponse(User user, Group group, GroupMembership groupMembership);
}

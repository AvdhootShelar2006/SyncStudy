package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.UpdateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto.*;
import com.avdhoot.StudyGroupFinderAPI.entity.*;
import com.avdhoot.StudyGroupFinderAPI.exception.AlreadyExistsException;
import com.avdhoot.StudyGroupFinderAPI.exception.DuplicateResourceException;
import com.avdhoot.StudyGroupFinderAPI.exception.EntityAndRelationshipsFinder;
import com.avdhoot.StudyGroupFinderAPI.exception.ResourceNotFoundException;
import com.avdhoot.StudyGroupFinderAPI.mapper.GroupMapper;
import com.avdhoot.StudyGroupFinderAPI.mapper.MembershipMapper;
import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.CreateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.GroupResponseDto;
import com.avdhoot.StudyGroupFinderAPI.repository.RoleRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupMembershipRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupMapper groupMapper;
    private final MembershipMapper membershipMapper;
    private final EntityAndRelationshipsFinder entityRelationshipsFinder;
    private final RoleRepository roleRepository;

    public GroupResponseDto createGroup(CreateGroupRequestDto requestDto, User user) {
        Group group = groupMapper.toEntity(requestDto);

        if(groupRepository.existsByGroupName(group.getGroupName())){
            throw new DuplicateResourceException("Group with name: " + group.getGroupName() + " already exist.");
        }
        Roles groupAdminRole = roleRepository
                .findByName("ROLE_GROUP_ADMIN")
                .orElseThrow();

        Set<Roles> roles = new HashSet<>();
        roles.add(groupAdminRole);

        group.setIsOpen(true);

        GroupMembership membership = membershipMapper.createMembership(user, group, roles);

        groupRepository.save(group);
        groupMembershipRepository.save(membership);

        GroupResponseDto responseDto = groupMapper.toGroupResponseDto(group);

        return responseDto;
    }

    public GroupResponseDto updateGroup(int groupId, UpdateGroupRequestDto updateGroup) {
        Group existingGroup = entityRelationshipsFinder.getGroupOrThrow(groupId);

        // Used if statements because if used methods like model groupMapper it can accidentally update something which user should not
        if (updateGroup.name() != null) {
            existingGroup.setGroupName(updateGroup.name());
        }
        if (updateGroup.subject() != null) {
            existingGroup.setSubject(updateGroup.subject());
        }
        if (updateGroup.field() != null) {
            existingGroup.setField(updateGroup.field());
        }
        if (updateGroup.description() != null) {
            existingGroup.setDescription(updateGroup.description());
        }
        if (updateGroup.tags() != null) {
            String existingTags = existingGroup.getTags() + ","+ updateGroup.tags();
            existingGroup.setTags(existingTags);
        }
        if (updateGroup.maxMembers() != null) {
            existingGroup.setMaxMembers(updateGroup.maxMembers());
        }

        GroupResponseDto responseDto = groupMapper.toGroupResponseDto(existingGroup);
        groupRepository.save(existingGroup);

        return responseDto;
    }

    public Page<GroupResponseDto> getAllGroups(Pageable pageable) {

        Page<Group> groups = groupRepository.findAll(pageable);

        return groups.map(groupMapper::toGroupResponseDto);
    }

    public GroupResponseDto getGroupById(int groupId) {
        Group group = entityRelationshipsFinder.getGroupOrThrow(groupId);
        return groupMapper.toGroupResponseDto(group);
    }

    public Page<GroupMemberDetailsResponse> getAllGroupMembers(Pageable pageable, int groupId) {
        entityRelationshipsFinder.getGroupOrThrow(groupId);

        Page<GroupMembership> memberships = groupMembershipRepository.findByGroup_Id(pageable,groupId);

        return memberships.map(membershipMapper::toGroupMemberDetailsResponse);
    }

    public LeaveResponseDto leaveGroup(int groupId, LeaveRequestDto request){
            User user = entityRelationshipsFinder.getUserOrThrow(request.memberId());
            Group group = entityRelationshipsFinder.getGroupOrThrow(groupId);

            GroupMembership membership = groupMembershipRepository
                    .findByGroup_IdAndUser_Id(group.getId(), user.getId())
                    .orElseThrow(
                            ()->new ResourceNotFoundException("User with Id: " + user.getId() +" no longer part of the Group")
                    );

            groupMembershipRepository.delete(membership);

            LeaveResponseDto leaveResponse = new  LeaveResponseDto(
                    user.getId(),
                    user.getName(),
                    group.getId(),
                    LocalDateTime.now()
            );
        return leaveResponse;
    }

    public List<Group> searchGroups(String keyword) {
        return groupRepository.searchUsingKeyword(keyword);
    }

    public JoinGroupResponse joinGroup(int groupId, User user) {

        Group group = entityRelationshipsFinder.getGroupOrThrow(groupId);

        if(groupMembershipRepository.existsByGroup_IdAndUser_Id(group.getId(), user.getId())){
            throw new AlreadyExistsException("User with id: " + user.getId() + " already exists in the group");
        }
        Roles memberRole = roleRepository
                .findByName("ROLE_MEMBER")
                .orElseThrow();

        Set<Roles> roles = new HashSet<>();
        roles.add(memberRole);

        GroupMembership groupMembership = membershipMapper.createMembership(user, group,roles );

        JoinGroupResponse joinGroupResponse = membershipMapper.toJoinGroupResponse(user, group, groupMembership);

        groupMembershipRepository.save(groupMembership);
        return joinGroupResponse;
    }
}

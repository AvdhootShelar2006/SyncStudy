package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.UpdateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto.*;
import com.avdhoot.StudyGroupFinderAPI.entity.*;
import com.avdhoot.StudyGroupFinderAPI.enums.GroupRole;
import com.avdhoot.StudyGroupFinderAPI.exception.*;
import com.avdhoot.StudyGroupFinderAPI.mapper.GroupMapper;
import com.avdhoot.StudyGroupFinderAPI.mapper.MembershipMapper;
import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.CreateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.GroupResponseDto;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupMembershipRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupMapper groupMapper;
    private final MembershipMapper membershipMapper;
    private final EntityAndRelationshipsFinder entityRelationshipsFinder;

    @Transactional
    public GroupResponseDto createGroup(CreateGroupRequestDto requestDto, User user) {
        Group group = groupMapper.toEntity(requestDto);

        if(groupRepository.existsByGroupName(group.getGroupName())){
            throw new DuplicateResourceException("Group with name: " + group.getGroupName() + " already exist.");
        }
        group.setIsOpen(true);
        group.setCreatedBy(user.getUsername());
        group.setIsEnable(true);
        group.setTotalMembers(1);
        GroupMembership membership = membershipMapper.createMembership(user, group, GroupRole.OWNER);
        groupRepository.save(group);
        groupMembershipRepository.save(membership);

        GroupResponseDto responseDto = groupMapper.toGroupResponseDto(group);
        return responseDto;
    }

    @Transactional
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

    @Transactional
    public LeaveResponseDto leaveGroup(int groupId, int userId ){
            User user = entityRelationshipsFinder.getUserOrThrow(userId);
            Group group = entityRelationshipsFinder.getGroupOrThrow(groupId);

            GroupMembership membership = groupMembershipRepository
                    .findByGroup_IdAndUser_Id(group.getId(), user.getId())
                    .orElseThrow(
                            ()->new ResourceNotFoundException("User with Id: " + user.getId() +" no longer part of the Group")
                    );
        if (membership.getGroupRole() == GroupRole.OWNER) {
            throw new ConflictOperationException("The community owner cannot leave until ownership transfer is supported.");
        }
        groupMembershipRepository.delete(membership);
        Integer totalMembers = group.getTotalMembers();
        group.setTotalMembers(Math.max(0, (totalMembers == null ? 1 : totalMembers) - 1));
        LeaveResponseDto leaveResponse = new  LeaveResponseDto(
                    user.getId(),
                    user.getName(),
                    group.getId(),
                    LocalDateTime.now()
        );
        return leaveResponse;
    }

    public Page<GroupResponseDto> searchGroups(Pageable pageable, String keyword) {
        String pattern = "%" + keyword.trim().toLowerCase() + "%";
        return groupRepository.searchUsingKeyword(pattern, pageable).map(groupMapper::toGroupResponseDto);
    }

    @Transactional
    public JoinGroupResponse joinGroup(int groupId, User user) {
        Group group = entityRelationshipsFinder.getGroupOrThrow(groupId);
        Integer totalMembers = group.getTotalMembers();

        if (!group.getIsEnable()) {
            throw new GroupNotActiveException("Group " + group.getGroupName() + " is currently disabled. You cannot join this group.");
        }
        if (totalMembers >= group.getMaxMembers()) {
            throw new GroupFullException("Group maximum number of members exceeded");
        }
        if(groupMembershipRepository.existsByGroup_IdAndUser_Id(group.getId(), user.getId())){
            throw new AlreadyExistsException("User with id: " + user.getId() + " already exists in the group");
        }
        totalMembers = totalMembers + 1;
        group.setTotalMembers(totalMembers);

        GroupMembership groupMembership = membershipMapper.createMembership(user, group, GroupRole.MEMBER);
        groupMembershipRepository.save(groupMembership);

        JoinGroupResponse joinGroupResponse = membershipMapper.toJoinGroupResponse(user, group, groupMembership);
        return joinGroupResponse;
    }

    public Page<GroupResponseDto> getAllGroupsUserIsJoinedIn(Pageable pageable, CustomUserDetails userDetails){
        Page<Group> groups = groupMembershipRepository.findGroupsByUserId(userDetails.getId(), pageable);
        return groups.map(groupMapper::toGroupResponseDto);
    }

    @Transactional
    public void deleteGroup(Integer groupId) {
        Group group = entityRelationshipsFinder.getGroupOrThrow(groupId);
        groupRepository.delete(group);
    }

    @Transactional
    public void removeMember(int groupId, int userId) {
        Group group =  entityRelationshipsFinder.getGroupOrThrow(groupId);
        GroupMembership groupMembership = groupMembershipRepository.findByGroup_IdAndUser_Id(group.getId(), userId).orElseThrow(
            () -> new ResourceNotFoundException("Community member was not found."));
        if (groupMembership.getGroupRole() == GroupRole.OWNER) {
            throw new ConflictOperationException("The community owner cannot be removed.");
        }

        groupMembershipRepository.delete(groupMembership);
        group.setTotalMembers(Math.max(0, (group.getTotalMembers() == null ? 1 : group.getTotalMembers()) - 1));
    }
}

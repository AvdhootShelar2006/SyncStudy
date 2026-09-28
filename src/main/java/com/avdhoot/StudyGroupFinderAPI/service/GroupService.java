package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.UpdateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto.*;
import com.avdhoot.StudyGroupFinderAPI.entity.Group;
import com.avdhoot.StudyGroupFinderAPI.exception.AlreadyExistsException;
import com.avdhoot.StudyGroupFinderAPI.exception.DuplicateResourceException;
import com.avdhoot.StudyGroupFinderAPI.exception.EntityAndRelationshipsFinder;
import com.avdhoot.StudyGroupFinderAPI.exception.ResourceNotFoundException;
import com.avdhoot.StudyGroupFinderAPI.mapper.GroupMapper;
import com.avdhoot.StudyGroupFinderAPI.mapper.MembershipMapper;
import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.CreateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.GroupResponseDto;
import com.avdhoot.StudyGroupFinderAPI.entity.GroupMembership;
import com.avdhoot.StudyGroupFinderAPI.entity.User;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupMembershipRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupMapper groupMapper;
    private final MembershipMapper membershipMapper;
    private final EntityAndRelationshipsFinder entityRelationshipsFinder;

    public GroupResponseDto createGroup(CreateGroupRequestDto requestDto) {
        Group group = groupMapper.toEntity(requestDto);

        if(groupRepository.existsByName(group.getName())){
            throw new DuplicateResourceException("Group with name: " + group.getName() + " already exist.");
        }
        group.setIsOpen(true);
        groupRepository.save(group);

        GroupResponseDto responseDto = groupMapper.toGroupResponseDto(group);

        return responseDto;
    }

    public GroupResponseDto updateGroup(int groupId, UpdateGroupRequestDto updateGroup) {
        Group existingGroup = entityRelationshipsFinder.getGroupOrThrow(groupId);

        // Used if statements because if used methods like model groupMapper it can accidentally update something which user should not
        if (updateGroup.name() != null) {
            existingGroup.setName(updateGroup.name());
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
            User user = entityRelationshipsFinder.getMemberOrThrow(request.memberId());
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

    public JoinGroupResponse joinGroup(int groupId, JoinGroupRequest request) {

        Group group = entityRelationshipsFinder.getGroupOrThrow(groupId);

        User user = entityRelationshipsFinder.getMemberOrThrow(request.memberId());

        if(groupMembershipRepository.existsByGroup_IdAndUser_Id(group.getId(), user.getId())){
            throw new AlreadyExistsException("User with id: " + user.getId() + " already exists in the group");
        }

        GroupMembership groupMembership = membershipMapper.createMembership(user, group);
        groupMembershipRepository.save(groupMembership);

        JoinGroupResponse joinGroupResponse = new JoinGroupResponse(
                user.getId(),
                user.getName(),
                group.getId(),
                groupMembership.getJoinedAt()
        );
        return joinGroupResponse;
    }
}

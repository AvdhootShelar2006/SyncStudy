package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.UpdateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto.*;
import com.avdhoot.StudyGroupFinderAPI.exception.AlreadyExistsException;
import com.avdhoot.StudyGroupFinderAPI.exception.DuplicateResourceException;
import com.avdhoot.StudyGroupFinderAPI.exception.EntityAndRelationshipsFinder;
import com.avdhoot.StudyGroupFinderAPI.exception.ResourceNotFoundException;
import com.avdhoot.StudyGroupFinderAPI.mapper.GroupMapper;
import com.avdhoot.StudyGroupFinderAPI.mapper.MembershipMapper;
import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.CreateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.GroupResponseDto;
import com.avdhoot.StudyGroupFinderAPI.entity.GroupMembership;
import com.avdhoot.StudyGroupFinderAPI.entity.Member;
import com.avdhoot.StudyGroupFinderAPI.entity.StudyGroup;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupMembershipRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupMapper groupMapper;
    private final MembershipMapper membershipMapper;
    private final EntityAndRelationshipsFinder entityRelationshipsFinder;

    public GroupResponseDto createGroup(CreateGroupRequestDto requestDto) {
        StudyGroup studyGroup = groupMapper.toEntity(requestDto);

        if(groupRepository.existsByName(studyGroup.getName())){
            throw new DuplicateResourceException("Group with name: " + studyGroup.getName() + " already exist.");
        }
        StudyGroup savedStudyGroup = new StudyGroup();
        savedStudyGroup.setIsOpen(true);
        groupRepository.save(savedStudyGroup);

        GroupResponseDto responseDto = groupMapper.toGroupResponseDto(savedStudyGroup);

        return responseDto;
    }

    public GroupResponseDto updateGroup(int groupId, UpdateGroupRequestDto updateGroup) {
        StudyGroup existingGroup = entityRelationshipsFinder.getGroupOrThrow(groupId);

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

    public List<GroupResponseDto> getAllGroups() {
        List<StudyGroup> groups = groupRepository.findAll();
        return groupMapper.toGroupResponseDtoList(groups);
    }

    public GroupResponseDto getGroupById(int groupId) {
        StudyGroup group = entityRelationshipsFinder.getGroupOrThrow(groupId);
        return groupMapper.toGroupResponseDto(group);
    }

    public List<GroupMemberDetailsResponse> getAllGroupMembers(int groupId) {
        entityRelationshipsFinder.getGroupOrThrow(groupId);

        List<GroupMembership> memberships = groupMembershipRepository.findByGroup_Id(groupId);

        return membershipMapper.toGroupMemberDetailsResponses(memberships);
    }

    public LeaveResponseDto leaveGroup(int groupId, LeaveRequestDto request){
            Member member = entityRelationshipsFinder.getMemberOrThrow(request.memberId());
            StudyGroup group = entityRelationshipsFinder.getGroupOrThrow(groupId);

            GroupMembership membership = groupMembershipRepository
                    .findByGroup_IdAndMember_Id(group.getId(), member.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("This member is not in this group!"));

            groupMembershipRepository.delete(membership);

            LeaveResponseDto leaveResponse = new  LeaveResponseDto(
                    member.getId(),
                    member.getName(),
                    group.getId(),
                    LocalDateTime.now()
            );
        return leaveResponse;
    }

    public List<StudyGroup> searchGroups(String keyword) {
        return groupRepository.searchUsingKeyword(keyword);
    }

    public JoinGroupResponse joinGroup(int groupId, JoinGroupRequest request) {
        StudyGroup group = entityRelationshipsFinder.getGroupOrThrow(groupId);
        Member member = entityRelationshipsFinder.getMemberOrThrow(request.memberId());

        if(groupMembershipRepository.existsByGroup_IdAndMember_Id(group.getId(), member.getId())){
            throw new AlreadyExistsException("Member with id: " + member.getId() + " already exists in the group");
        }

        GroupMembership groupMembership = membershipMapper.createMembership(member, group);
        groupMembershipRepository.save(groupMembership);

        JoinGroupResponse joinGroupResponse = new JoinGroupResponse(
                member.getId(),
                member.getName(),
                group.getId(),
                groupMembership.getJoinedAt()
        );
        return joinGroupResponse;
    }
}

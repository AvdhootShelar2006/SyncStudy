package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto.GroupMemberDetailsResponse;
import com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto.JoinLeaveRequest;
import com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto.JoinLeaveResponse;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupMapper groupMapper;
    private final MembershipMapper membershipMapper;


    public GroupResponseDto createGroup(CreateGroupRequestDto requestDto) {
        StudyGroup studyGroup = groupMapper.toEntity(requestDto);
        StudyGroup savedStudyGroup = new StudyGroup();
        savedStudyGroup.setIsOpen(true);
        groupRepository.save(studyGroup);

        GroupResponseDto responseDto = groupMapper.toDto(savedStudyGroup);

        return responseDto;
    }

    public List<GroupResponseDto> getAllGroups() {
        List<StudyGroup> groups = groupRepository.findAll();
        return groupMapper.toDtoList(groups);
    }

    public GroupResponseDto getGroupById(int groupId) {
        StudyGroup group = getGroupOrThrow(groupId);
        return groupMapper.toDto(group);
    }

    public List<GroupMemberDetailsResponse> getAllGroupMembers(int groupId) {

        List<GroupMembership> memberships = groupMembershipRepository.findByGroup_Id(groupId);
        return membershipMapper.toDtoList(memberships);
    }

    public JoinLeaveResponse leaveGroup(int groupId, JoinLeaveRequest request){
            int memberId = request.memberId();

            GroupMembership membership = groupMembershipRepository
                    .findByGroup_IdAndMember_Id(groupId, memberId)
                    .orElseThrow(() -> new RuntimeException("This member is not in this group!"));

            groupMembershipRepository.delete(membership);

            return new JoinLeaveResponse("Left Group Gracefully");
    }

    public StudyGroup updateGroup(int groupId, StudyGroup newGroupData) {
        StudyGroup existingGroup = getGroupOrThrow(groupId);

        // Used if statements because if used methods like model groupMapper it can accidentally update something which user should not
        if (newGroupData.getName() != null) {
            existingGroup.setName(newGroupData.getName());
        }
        if (newGroupData.getSubject() != null) {
            existingGroup.setSubject(newGroupData.getSubject());
        }
        if (newGroupData.getField() != null) {
            existingGroup.setField(newGroupData.getField());
        }
        if (newGroupData.getDescription() != null) {
            existingGroup.setDescription(newGroupData.getDescription());
        }
        if (newGroupData.getTags() != null) {
            existingGroup.setTags(newGroupData.getTags());
        }
        if (newGroupData.getIsOpen() != null) {
            existingGroup.setIsOpen(newGroupData.getIsOpen());
        }
        if (newGroupData.getMaxMembers() != null) {
            existingGroup.setMaxMembers(newGroupData.getMaxMembers());
        }
        return groupRepository.save(existingGroup);
    }

    public List<StudyGroup> searchGroups(String keyword) {
        return groupRepository.searchUsingKeyword(keyword);
    }

    public List<JoinLeaveResponse> joinGroup(int groupId, List<JoinLeaveRequest> request) {
            StudyGroup group = getGroupOrThrow(groupId);
            List<Integer> memberIDs = request
                    .stream()
                    .map(JoinLeaveRequest::memberId)
                    .distinct()
                    .toList();

            List<Member> members = memberRepository.findAllById(memberIDs);
            if(group.getIsOpen()){
                List<GroupMembership> memberships = members
                        .stream()
                        .filter(member -> !groupMembershipRepository.existsByGroup_IdAndMember_Id(groupId, member.getId()))
                        .map(member -> membershipMapper.createMembership(member, group))
                        .toList();

                if(!memberships.isEmpty()){
                    groupMembershipRepository.saveAll(memberships);
                }
            }
            return members
                    .stream()
                    .map(m-> new JoinLeaveResponse(m.getName()))
                    .toList();
    }

    private StudyGroup getGroupOrThrow(int id){
        return groupRepository.findById(id).orElseThrow(()-> new RuntimeException("Group Not Found or does not exist"));
    }

    private boolean exitsByName(StudyGroup group){
        return groupRepository.existsByName(group.getName());
    }
}

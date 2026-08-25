package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.mapper.GroupMapper;
import com.avdhoot.StudyGroupFinderAPI.mapper.MembershipMapper;
import com.avdhoot.StudyGroupFinderAPI.model.dto.groupDto.CreateGroupRequestDto;
import com.avdhoot.StudyGroupFinderAPI.model.dto.groupDto.GroupResponseDto;
import com.avdhoot.StudyGroupFinderAPI.model.dto.groupMemberDto.*;
import com.avdhoot.StudyGroupFinderAPI.model.entity.GroupMembership;
import com.avdhoot.StudyGroupFinderAPI.model.entity.Member;
import com.avdhoot.StudyGroupFinderAPI.model.entity.StudyGroup;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupMembershipRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        StudyGroup savedStudyGroup = groupRepository.save(studyGroup);

        GroupResponseDto responseDto = groupMapper.toDto(savedStudyGroup);
        responseDto.setMessage("Group created Successfully");

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



//    public  List<GroupMemberDetailsResponse> filterMemberByDate(int groupId, LocalDate startDate, Optional<LocalDate> endDate, Pageable pageable) {
//        StudyGroup group = getGroupOrThrow(groupId);
//        List<GroupMembership> groupMemberships = null;
//        if(endDate.isEmpty()){
//            groupMemberships  = groupMembershipRepository.findByGroup_IdAndJoinedAtAfter(groupId, startDate, pageable);
//        } else{
//            groupMemberships  = groupMembershipRepository.findByGroup_IdAndJoinedAtBetween(groupId, startDate, endDate, pageable);
//        }
//        return membershipMapper.toDtoList(groupMemberships);
//    }

    private StudyGroup getGroupOrThrow(int id){
        return groupRepository.findById(id).orElseThrow(()-> new RuntimeException("Group Not Found or does not exist"));
    }
}

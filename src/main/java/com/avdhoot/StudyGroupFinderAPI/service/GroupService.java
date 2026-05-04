package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.model.dto.group_member_dto.GroupMemberDetailsResponse;
import com.avdhoot.StudyGroupFinderAPI.model.entities.GroupMembership;
import com.avdhoot.StudyGroupFinderAPI.model.entities.Member;
import com.avdhoot.StudyGroupFinderAPI.model.entities.StudyGroup;
import com.avdhoot.StudyGroupFinderAPI.model.dto.group_member_dto.JoinLeaveRequest;
import com.avdhoot.StudyGroupFinderAPI.model.dto.group_member_dto.JoinLeaveResponse;
import com.avdhoot.StudyGroupFinderAPI.repository.GroupMembershipRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.GroupRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GroupMembershipRepository groupMembershipRepository;

    public StudyGroup createGroup(StudyGroup studyGroup) {
        return groupRepository.save(studyGroup);
    }

    public List<StudyGroup> getAllGroups() {
        return groupRepository.findAll();
    }

    public StudyGroup getGroupById(int id) {
        return groupRepository.findById(id).orElse(new StudyGroup(-1));
    }

    public JoinLeaveResponse joinGroup(int groupId, JoinLeaveRequest request) {
        StudyGroup group = groupRepository.
                findById(groupId)
                .orElseThrow(()-> new RuntimeException("Group Not Found!"));

        Member member = memberRepository
                .findById(request.memberId())
                .orElseThrow(() -> new RuntimeException("Member not Found!"));

        if(group.getIsOpen()){
            GroupMembership membership = new GroupMembership();

            membership.setMember(member);
            membership.setGroup(group);
            membership.setJoinedAt(LocalDate.now());

            groupMembershipRepository.save(membership);
            return new JoinLeaveResponse(" Successfully joined the group!");
        }
        else{
            return new JoinLeaveResponse("Couldn't Join Group!");
        }
    }


    public List<GroupMemberDetailsResponse> getAllGroupMembers(int groupId) {

        StudyGroup group = groupRepository.
                findById(groupId)
                .orElseThrow(()-> new RuntimeException("Group Not Found!"));

        List<GroupMembership> memberships = groupMembershipRepository.findByGroup_Id(groupId);
        List<GroupMemberDetailsResponse> responses = new ArrayList<>();

        for(GroupMembership membership : memberships){
            GroupMemberDetailsResponse response = new GroupMemberDetailsResponse(
                    membership.getMember().getName()
            );
            responses.add(response);
        }

        return responses;
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
        StudyGroup existingGroup = groupRepository.findById(groupId).orElseThrow(()->new RuntimeException("Failed Request"));


        // Used if statements because if used methods like model mapper it can accidentally update something which user should not
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
}

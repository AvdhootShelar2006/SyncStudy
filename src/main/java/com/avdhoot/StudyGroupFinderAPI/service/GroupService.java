package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.model.Report;
import com.avdhoot.StudyGroupFinderAPI.model.dto.group_member_dto.*;
import com.avdhoot.StudyGroupFinderAPI.model.entities.GroupMembership;
import com.avdhoot.StudyGroupFinderAPI.model.entities.Member;
import com.avdhoot.StudyGroupFinderAPI.model.entities.StudyGroup;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupMembershipRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.MemberRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import javax.management.RuntimeMBeanException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GroupMembershipRepository groupMembershipRepository;


    @Autowired
    private ReportRepository reportRepository;

    public StudyGroup createGroup(StudyGroup studyGroup) {
        return groupRepository.save(studyGroup);
    }

    public List<StudyGroup> getAllGroups() {
        return groupRepository.findAll();
    }

    public StudyGroup getGroupById(int id) {
        return groupRepository.findById(id).orElse(new StudyGroup(-1));
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

    public List<StudyGroup> searchGroups(String keyword) {
        return groupRepository.searchUsingKeyword(keyword);
    }

    public  List<GroupMemberDetailsResponse> filterMemberByDate(int groupId, LocalDate startDate, Optional<LocalDate> endDate, Pageable pageable) {

        StudyGroup group = groupRepository.
                findById(groupId)
                .orElseThrow(()-> new RuntimeException());

        List<GroupMembership> groupMemberships = new ArrayList<>();

        if(endDate.isEmpty()){
            groupMemberships  = groupMembershipRepository.findByGroup_IdAndJoinedAtAfter(groupId, startDate, pageable);
        } else{
            groupMemberships  = groupMembershipRepository.findByGroup_IdAndJoinedAtBetween(groupId, startDate, endDate, pageable);
        }

        List<GroupMemberDetailsResponse> members = new ArrayList<>();

        for(GroupMembership membership : groupMemberships){
           GroupMemberDetailsResponse response = new GroupMemberDetailsResponse(
                   membership.getMember().getName()
           );
           members.add(response);
        }

        return members;
    }

    public void groupReport(int groupId, ReportRequest request) {
      if(!groupMembershipRepository.existsByGroup_IdAndMember_Id(groupId, request.reportedBy())){
          throw new RuntimeException("You are not a member of the group!");
      }

      if(!groupMembershipRepository.existsByGroup_IdAndMember_Id(groupId, request.targetMember())){
          throw new RuntimeException("The reported user is not a member of the group!");
      }
        Member reportedBy = memberRepository.findById(request.reportedBy())
                .orElseThrow(() -> new RuntimeException("Reporter does not exist"));

        Member targetMember = memberRepository.findById(request.targetMember())
                .orElseThrow(() -> new RuntimeException("Target member does not exist"));

        Report report = new Report();
        report.setReportedBy(reportedBy);
        report.setTargetMemberId(targetMember);
        report.setReason(request.reason());

      reportRepository.save(report);

    }

    public List<JoinLeaveResponse> joinGroup(int groupId, List<JoinLeaveRequest> request) {
            StudyGroup group = groupRepository.
                    findById(groupId)
                    .orElseThrow(()-> new RuntimeException("Group Not Found!"));
            List<Integer> memberIDs = request.stream().map(JoinLeaveRequest::memberId).toList();


            List<Member> members = memberRepository.findAllById(memberIDs);

            if(group.getIsOpen()){
                for(Member member : members) {
                    GroupMembership membership = new GroupMembership();

                    membership.setMember(member);
                    membership.setGroup(group);
                    membership.setJoinedAt(LocalDate.now());

                    groupMembershipRepository.save(membership);
                }

            }
            List<JoinLeaveResponse> responses = new ArrayList<>();

        for(Member member : members) {
            JoinLeaveResponse response = new JoinLeaveResponse(
                    member.getName()
            );
            responses.add(response);
        }

        return responses;

    }
}

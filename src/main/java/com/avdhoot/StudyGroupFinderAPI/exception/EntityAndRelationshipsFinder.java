package com.avdhoot.StudyGroupFinderAPI.exception;

import com.avdhoot.StudyGroupFinderAPI.entity.Member;
import com.avdhoot.StudyGroupFinderAPI.entity.StudyGroup;
import com.avdhoot.StudyGroupFinderAPI.repository.MemberRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityAndRelationshipsFinder {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;

    public StudyGroup getGroupOrThrow(Integer groupId){
        return groupRepository.findById(groupId).orElseThrow(()-> new ResourceNotFoundException("Group with id " + groupId + " not found."));
    }

    public Member getMemberOrThrow(Integer memberId){
        return memberRepository.findById(memberId).orElseThrow(()-> new ResourceNotFoundException("Member with id " + memberId + " not found."));
    }

}

package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.model.dto.group_member_dto.MemberDetailsResponse;
import com.avdhoot.StudyGroupFinderAPI.model.entities.Member;
import com.avdhoot.StudyGroupFinderAPI.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> addOrUpdateMember(List<Member> members) {
       return memberRepository.saveAll(members);
    }

    public MemberDetailsResponse getMemberById(int memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow();

        return new MemberDetailsResponse(
                member.getName(),
                member.getEmail(),
                member.getCreatedAt()
        );
    }
}

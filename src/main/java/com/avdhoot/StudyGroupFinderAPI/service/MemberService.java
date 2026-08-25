package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.model.dto.groupMemberDto.MemberDetailsResponse;
import com.avdhoot.StudyGroupFinderAPI.model.entity.Member;
import com.avdhoot.StudyGroupFinderAPI.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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

package com.avdhoot.StudyGroupFinderAPI.service;

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

    public Member getAllMemberById(int memberId) {
        return memberRepository.findById(memberId).orElse(new Member(-1));
    }
}

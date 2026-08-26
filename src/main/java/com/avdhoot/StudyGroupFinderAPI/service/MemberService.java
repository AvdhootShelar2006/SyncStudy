package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.dto.memberDto.CreateMemberDetailResponse;
import com.avdhoot.StudyGroupFinderAPI.dto.memberDto.CreateMemberRequestDto;
import com.avdhoot.StudyGroupFinderAPI.entity.Member;
import com.avdhoot.StudyGroupFinderAPI.mapper.MemberMapper;
import com.avdhoot.StudyGroupFinderAPI.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public CreateMemberDetailResponse getMemberById(int memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow();
        return memberMapper.toMemberResponseDto(member);
    }

    public CreateMemberDetailResponse createUser(CreateMemberRequestDto requestDto) {
        Member member = Member.builder()
                .name(requestDto.name())
                .email(requestDto.email())
                .password(requestDto.password())
                .createdAt(LocalDate.now())
                .build();
        memberRepository.save(member);
        return memberMapper.toMemberResponseDto(member);
    }
}

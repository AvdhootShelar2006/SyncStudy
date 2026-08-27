package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.dto.memberDto.CreateMemberDetailResponse;
import com.avdhoot.StudyGroupFinderAPI.dto.memberDto.CreateMemberRequestDto;
import com.avdhoot.StudyGroupFinderAPI.entity.Member;
import com.avdhoot.StudyGroupFinderAPI.exception.DuplicateResourceException;
import com.avdhoot.StudyGroupFinderAPI.exception.EntityAndRelationshipsFinder;
import com.avdhoot.StudyGroupFinderAPI.mapper.MemberMapper;
import com.avdhoot.StudyGroupFinderAPI.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final EntityAndRelationshipsFinder entityAndRelationshipsFinder;
    public CreateMemberDetailResponse getMemberById(int memberId) {
        Member member = entityAndRelationshipsFinder.getMemberOrThrow(memberId);
        return memberMapper.toMemberResponseDto(member);
    }

    public CreateMemberDetailResponse createUser(CreateMemberRequestDto requestDto) {
        if(memberRepository.existsByEmail(requestDto.email())){
            throw new DuplicateResourceException("User with email " + requestDto.email()+" already exits");
        }
        Member member = Member.builder()
                .name(requestDto.name())
                .email(requestDto.email())
                .password(requestDto.password())
                .createdAt(LocalDate.now())
                .build();
        memberRepository.save(member);
        return memberMapper.toMemberResponseDto(member);
    }

    public Page<CreateMemberDetailResponse> getAllMembers(Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);
        return members.map(member -> memberMapper.toMemberResponseDto(member));
    }
}

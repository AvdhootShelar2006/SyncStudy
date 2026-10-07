package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.dto.userDto.CreateUserDetailResponse;
import com.avdhoot.StudyGroupFinderAPI.dto.userDto.CreateUserRequestDto;
import com.avdhoot.StudyGroupFinderAPI.entity.CustomUserDetails;
import com.avdhoot.StudyGroupFinderAPI.entity.Roles;
import com.avdhoot.StudyGroupFinderAPI.entity.User;
import com.avdhoot.StudyGroupFinderAPI.exception.DuplicateResourceException;
import com.avdhoot.StudyGroupFinderAPI.exception.EntityAndRelationshipsFinder;
import com.avdhoot.StudyGroupFinderAPI.exception.ResourceNotFoundException;
import com.avdhoot.StudyGroupFinderAPI.mapper.MemberMapper;
import com.avdhoot.StudyGroupFinderAPI.repository.RoleRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MemberMapper memberMapper;
    private final RoleRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;


    public CreateUserDetailResponse registerUser(CreateUserRequestDto requestDto) {
        if(userRepository.existsByEmail(requestDto.email())){
            throw new DuplicateResourceException("User with email " + requestDto.email()+" already exits");
        }
        User user = new User();
        Roles roles = rolesRepository.findByName("ROLE_USER").orElseThrow(()-> new ResourceNotFoundException("USER not Found!!"));
        String encodedPassword = passwordEncoder.encode(requestDto.password());

        user.setName(requestDto.name());
        user.setUsername(requestDto.username());
        user.setPassword(encodedPassword);
        user.setEmail(requestDto.email());
        user.getRoles().add(roles);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        return memberMapper.toMemberResponseDto(user);
    }

}

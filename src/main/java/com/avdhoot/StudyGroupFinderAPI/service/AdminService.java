package com.avdhoot.StudyGroupFinderAPI.service;

import com.avdhoot.StudyGroupFinderAPI.dto.groupDto.GroupResponseDto;
import com.avdhoot.StudyGroupFinderAPI.dto.userDto.CreateUserDetailResponse;
import com.avdhoot.StudyGroupFinderAPI.entity.CustomUserDetails;
import com.avdhoot.StudyGroupFinderAPI.entity.Group;
import com.avdhoot.StudyGroupFinderAPI.entity.User;
import com.avdhoot.StudyGroupFinderAPI.exception.EntityAndRelationshipsFinder;
import com.avdhoot.StudyGroupFinderAPI.mapper.GroupMapper;
import com.avdhoot.StudyGroupFinderAPI.mapper.MemberMapper;
import com.avdhoot.StudyGroupFinderAPI.repository.UserRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final GroupMapper groupMapper;
    private final EntityAndRelationshipsFinder entityAndRelationshipsFinder;
    private final UserRepository userRepository;
    private final MemberMapper memberMapper;
    private final GroupRepository groupRepository;

    @Transactional
    public String disableOrEnableUser(int userId, boolean enabled) {
        User user =  entityAndRelationshipsFinder.getUserOrThrow(userId);
        user.setEnabled(enabled);
        return enabled ? "User with name "+ user.getUsername() + " is Successfully enable" : "User with name " + user.getUsername() + " is Successfully disable";
    }

    @Transactional
    public String disableOrEnableGroup(int groupId, boolean enabled) {
        Group group = entityAndRelationshipsFinder.getGroupOrThrow(groupId);
        group.setIsEnable(enabled);
        return enabled ? "Group with name " + group.getGroupName()+ " is Successfully enable" : "Group with name " + group.getGroupName()+" is Successfully disable";
    }

    public Page<CreateUserDetailResponse> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(member -> memberMapper.toMemberResponseDto(member));
    }

    public Page<GroupResponseDto> getAllSuspendedGroups(Pageable pageable) {
        Page<Group> suspendedGroups = groupRepository.findByIsEnableFalse(pageable);
        return suspendedGroups.map(group -> groupMapper.toGroupResponseDto(group));
    }

    public Page<CreateUserDetailResponse> getAllSuspendedUsers(Pageable pageable) {
        Page<User> users = userRepository.findByEnabledFalse(pageable);
        return users.map(member -> memberMapper.toMemberResponseDto(member));
    }
}



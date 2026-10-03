package com.avdhoot.StudyGroupFinderAPI.config;

import com.avdhoot.StudyGroupFinderAPI.entity.CustomUserDetails;
import com.avdhoot.StudyGroupFinderAPI.enums.GroupRole;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupMembershipRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.queryRepository.GroupQueryRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("groupSecurityConfig")
public class GroupSecurityConfig {

    private GroupMembershipRepository groupMembershipRepository;
    private GroupQueryRepository groupQueryRepository;

    public GroupSecurityConfig(GroupMembershipRepository groupMembershipRepository, GroupQueryRepository groupQueryRepository) {
        this.groupMembershipRepository = groupMembershipRepository;
        this.groupQueryRepository = groupQueryRepository;
    }

    public boolean hasRole(Authentication authentication, Integer groupId, GroupRole requiredRole) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUser().getId();
        return groupMembershipRepository.findByGroup_IdAndUser_Id(groupId, userId).map(m-> m.getGroupRole().ordinal() >= requiredRole.ordinal()).orElse(false);
    }

    public boolean isGroupMember(Authentication authentication, Integer groupId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUser().getId();
        return groupMembershipRepository
                .existsByGroup_IdAndUser_Id(
                groupId,
                userId
        );
    }

    public boolean isQueryOwner(Authentication authentication, Integer queryId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUser().getId();
        return groupQueryRepository.existsByPostedBy_IdAndQueryId(userId, queryId);
    }
}
package com.avdhoot.StudyGroupFinderAPI.config;

import com.avdhoot.StudyGroupFinderAPI.entity.CustomUserDetails;
import com.avdhoot.StudyGroupFinderAPI.entity.GroupMembership;
import com.avdhoot.StudyGroupFinderAPI.enums.GroupRole;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupMembershipRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.queryRepository.GroupQueryRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.queryRepository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("groupSecurityConfig")
@RequiredArgsConstructor
public class GroupSecurityConfig {

    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupQueryRepository groupQueryRepository;
    private final ReplyRepository replyRepository;

    public boolean hasRole(Authentication authentication, Integer groupId, GroupRole requiredRole) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUser().getId();
        return groupMembershipRepository.findByGroup_IdAndUser_Id(groupId, userId).map(groupMembership-> groupMembership.getGroupRole().ordinal() >= requiredRole.ordinal()).orElse(false);
    }
    public boolean canManageMember(Authentication authentication, Integer groupId, Integer targetUserId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer actorId = customUserDetails.getUser().getId();

        Optional<GroupMembership> actorMembership =  groupMembershipRepository.findByGroup_IdAndUser_Id(groupId, actorId);

        Optional<GroupMembership> targetMembership = groupMembershipRepository.findByGroup_IdAndUser_Id(groupId, targetUserId);

        return actorMembership.isPresent() && targetMembership.isPresent() && actorMembership.get().getGroupRole().ordinal() > targetMembership.get().getGroupRole().ordinal();
    }

    public boolean isQueryOwner(Authentication authentication, Integer queryId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUser().getId();
        return groupQueryRepository.existsByPostedBy_IdAndQueryId(userId, queryId);
    }

    public boolean isReplyOwner(Authentication authentication, Integer replyId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUser().getId();
        return replyRepository.existsByUser_IdAndReplyId(userId, replyId);
    }
}
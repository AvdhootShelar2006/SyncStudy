package com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto;

import java.time.LocalDate;

public record JoinGroupResponse(
        Integer memberId,
        String username,
        Integer groupId,
        LocalDate joinedAt
) {
}

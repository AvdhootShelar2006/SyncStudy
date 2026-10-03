package com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto;

import java.time.LocalDate;

public record GroupMemberDetailsResponse (
        Integer memberId,
        String username,
        LocalDate joinedAt
){
}

package com.avdhoot.StudyGroupFinderAPI.dto.groupMemberDto;

import java.time.LocalDate;

public record GroupMemberDetailsResponse (
        Integer userId,
        String username,
        LocalDate joinedAt
){
}

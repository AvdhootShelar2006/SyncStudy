package com.avdhoot.StudyGroupFinderAPI.model.dto.answer_query;

import com.avdhoot.StudyGroupFinderAPI.model.interaction.AnswerQuery;

public record AnswerQueryRequest(
        String content,
        Integer memberId
) {
}

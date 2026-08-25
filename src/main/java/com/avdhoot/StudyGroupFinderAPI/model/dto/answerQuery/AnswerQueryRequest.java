package com.avdhoot.StudyGroupFinderAPI.model.dto.answerQuery;

public record AnswerQueryRequest(
        String content,
        Integer memberId
) {
}

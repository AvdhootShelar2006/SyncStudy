package com.avdhoot.StudyGroupFinderAPI.dto.answerQuery;

public record AnswerQueryRequest(
        String content,
        Integer memberId
) {
}

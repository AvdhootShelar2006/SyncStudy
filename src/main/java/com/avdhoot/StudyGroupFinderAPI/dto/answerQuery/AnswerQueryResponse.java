package com.avdhoot.StudyGroupFinderAPI.dto.answerQuery;

public record AnswerQueryResponse(
        String content,
        String answeredByName,
        String queryTitle

) {
}

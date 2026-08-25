package com.avdhoot.StudyGroupFinderAPI.model.dto.answerQuery;

public record AnswerQueryResponse(
        String content,
        String answeredByName,
        String queryTitle

) {
}

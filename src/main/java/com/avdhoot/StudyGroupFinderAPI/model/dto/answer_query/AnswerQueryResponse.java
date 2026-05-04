package com.avdhoot.StudyGroupFinderAPI.model.dto.answer_query;

public record AnswerQueryResponse(
        String content,
        String answeredByName,
        String queryTitle

) {
}

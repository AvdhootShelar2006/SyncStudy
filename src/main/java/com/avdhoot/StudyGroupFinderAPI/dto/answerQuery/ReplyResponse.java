package com.avdhoot.StudyGroupFinderAPI.dto.answerQuery;

public record ReplyResponse(
        String content,
        String answeredByName,
        String queryTitle

) {
}

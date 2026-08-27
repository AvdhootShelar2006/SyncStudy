package com.avdhoot.StudyGroupFinderAPI.dto.exceptionDto;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationExceptionResponseDto (
        LocalDateTime timestamp,
        Integer statusCode,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors
){
}
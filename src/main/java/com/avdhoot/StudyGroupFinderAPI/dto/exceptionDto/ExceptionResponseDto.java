package com.avdhoot.StudyGroupFinderAPI.dto.exceptionDto;

import java.time.LocalDateTime;

public record ExceptionResponseDto (
    LocalDateTime timestamp,
    Integer statusCode,
    String error,
    String message,
    String path
){}

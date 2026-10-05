package com.avdhoot.StudyGroupFinderAPI.exception;

public class GroupFullException extends RuntimeException {
    public GroupFullException(String message) {
        super(message);
    }
}


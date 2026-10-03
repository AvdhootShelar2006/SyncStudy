package com.avdhoot.StudyGroupFinderAPI.exception;

public class GroupNotActiveException extends RuntimeException {
    public GroupNotActiveException(String message) {
        super(message);
    }
}

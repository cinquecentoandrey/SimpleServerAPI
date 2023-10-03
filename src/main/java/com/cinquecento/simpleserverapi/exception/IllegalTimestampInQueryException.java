package com.cinquecento.simpleserverapi.exception;

public class IllegalTimestampInQueryException extends RuntimeException{
    public IllegalTimestampInQueryException(String message) {
        super(message);
    }
}

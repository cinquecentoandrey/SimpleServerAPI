package com.cinquecento.simpleserverapi.util.exception;

public class IllegalTimestampInQueryException extends RuntimeException{
    public IllegalTimestampInQueryException(String message) {
        super(message);
    }
}

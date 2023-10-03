package com.cinquecento.simpleserverapi.exception;

public class IncorrectStatusException extends RuntimeException {
    public IncorrectStatusException(String message) {
        super(message);
    }
}

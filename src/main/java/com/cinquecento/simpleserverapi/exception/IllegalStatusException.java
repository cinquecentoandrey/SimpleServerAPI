package com.cinquecento.simpleserverapi.exception;

public class IllegalStatusException extends RuntimeException{
    public IllegalStatusException(String message) {
        super(message);
    }
}

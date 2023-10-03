package com.cinquecento.simpleserverapi.exception;

public class IllegalFieldForSortException extends RuntimeException{
    public IllegalFieldForSortException(String message) {
        super(message);
    }
}

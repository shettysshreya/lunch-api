package com.task.lunch.exception;

/**
 * Custom exception to wrap all service layer exceptions
 */
public class LunchApiException extends Exception {
    public LunchApiException(String message) {
        super(message);
    }
}

package com.globalitgeeks.examninja.exception;



public class InvalidExamRequestException extends RuntimeException {
    public InvalidExamRequestException(String message) {
        super(message);
    }

    public InvalidExamRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}

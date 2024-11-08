package com.globalitgeeks.examninja.exception;


public class ExamDataBaseOperationException extends RuntimeException {
    public ExamDataBaseOperationException(String message) {
            super(message);
        }


    public ExamDataBaseOperationException(String message, Throwable cause) {
        super(message, cause); // Sets the message and the cause (the original exception).
    }
    }

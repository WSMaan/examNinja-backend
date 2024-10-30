package com.globalitgeeks.examninja.exception;

public class NotValidNumberException extends IllegalArgumentException {
    public NotValidNumberException (String message) {
        super(message);
    }
}

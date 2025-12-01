package com.example.holidaykeeper.support.exception;

public class ApiCallFailedException extends RuntimeException {
    public ApiCallFailedException(String message) {
        super(message);
    }
}

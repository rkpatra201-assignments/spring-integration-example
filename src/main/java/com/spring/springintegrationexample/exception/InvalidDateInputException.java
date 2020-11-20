package com.spring.springintegrationexample.exception;

public class InvalidDateInputException extends RuntimeException {
    public InvalidDateInputException(String message) {
        super(message);
    }
}

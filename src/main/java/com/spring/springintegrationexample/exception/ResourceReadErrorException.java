package com.spring.springintegrationexample.exception;

public class ResourceReadErrorException extends RuntimeException{
    public ResourceReadErrorException(String message) {
        super(message);
    }
}

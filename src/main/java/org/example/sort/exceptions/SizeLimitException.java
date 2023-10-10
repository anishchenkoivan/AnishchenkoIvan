package org.example.sort.exceptions;

public class SizeLimitException extends RuntimeException{
    public SizeLimitException(String message) {
        super(message);
    }
}

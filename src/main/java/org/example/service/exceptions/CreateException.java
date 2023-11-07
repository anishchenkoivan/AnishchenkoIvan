package org.example.service.exceptions;

public class CreateException extends RuntimeException {
    public CreateException(String message, Throwable cause) {
        super(message, cause);
    }
}

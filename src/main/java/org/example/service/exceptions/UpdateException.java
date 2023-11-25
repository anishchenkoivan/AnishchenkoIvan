package org.example.service.exceptions;

public class UpdateException extends RuntimeException {
    public UpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}

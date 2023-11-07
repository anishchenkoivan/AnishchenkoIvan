package org.example.service.exceptions;

public class DeleteException extends RuntimeException {
    public DeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteException(String message) {
    }
}

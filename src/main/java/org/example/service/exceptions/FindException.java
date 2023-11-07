package org.example.service.exceptions;

public class FindException extends RuntimeException {
    public FindException(String message, Throwable cause) {
        super(message, cause);
    }
}

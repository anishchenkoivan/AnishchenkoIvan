package org.example.sort.exceptions;

public class FailedSortingException extends RuntimeException{
    public FailedSortingException(String message) {
        super(message);
    }
}

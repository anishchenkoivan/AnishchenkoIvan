package org.example.sort.exceptions;

public class AbsentSorterException extends RuntimeException{
    public AbsentSorterException(String message) {
        super(message);
    }
}

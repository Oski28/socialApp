package com.example.socialApp.exceptions;

public class OperationAccessDeniedException extends RuntimeException {
    public OperationAccessDeniedException(String message) {
        super(message);
    }
}

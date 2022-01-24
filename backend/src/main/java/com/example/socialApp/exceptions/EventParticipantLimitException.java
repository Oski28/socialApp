package com.example.socialApp.exceptions;

public class EventParticipantLimitException extends RuntimeException {
    public EventParticipantLimitException(String message) {
        super(message);
    }
}

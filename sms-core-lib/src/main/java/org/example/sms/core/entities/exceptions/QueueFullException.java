package org.example.sms.core.entities.exceptions;

public class QueueFullException extends RuntimeException {

    public QueueFullException(String message) {
        super(message);
    }
}
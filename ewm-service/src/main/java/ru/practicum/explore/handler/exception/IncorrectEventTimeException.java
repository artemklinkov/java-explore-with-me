package ru.practicum.explore.handler.exception;

public class IncorrectEventTimeException extends RuntimeException {

    public IncorrectEventTimeException(String message) {
        super(message);
    }
}

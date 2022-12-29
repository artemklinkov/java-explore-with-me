package ru.practicum.explore.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ResponseEntity<ApiError> handleInternalError(Exception ex) {
        log.warn("Error occurred. Message: {}", ex.getMessage());
        ApiError error = ApiError.fromException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(error.getStatus()).body(error);
    }
}

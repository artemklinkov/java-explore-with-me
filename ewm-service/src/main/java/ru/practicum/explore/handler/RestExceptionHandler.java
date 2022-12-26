package ru.practicum.explore.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore.handler.exception.*;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(Exception ex) {
        ApiError error = ApiError.fromException(ex, HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundRequest(Exception ex) {
        ApiError error = ApiError.fromException(ex, HttpStatus.NOT_FOUND);
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler({
            IncorrectEventTimeException.class,
            NotAvailableException.class,
            AccessLevelException.class})
    public ResponseEntity<ApiError> handleConditionsAreNotMet(Exception ex) {
        ApiError error = ApiError.fromException(ex, HttpStatus.FORBIDDEN);
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(Exception ex) {
        ApiError error = ApiError.fromException(ex, HttpStatus.CONFLICT);
        return ResponseEntity.status(error.getStatus()).body(error);
    }
}

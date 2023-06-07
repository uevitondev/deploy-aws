package com.uevitondev.catalog.resources.exceptions;

import com.uevitondev.catalog.services.exceptions.DatabaseException;
import com.uevitondev.catalog.services.exceptions.PageablePropertyException;
import com.uevitondev.catalog.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFoundException(ResourceNotFoundException e, HttpServletRequest servletRequest) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError standardError = new StandardError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(status.value());
        standardError.setError("Not found!");
        standardError.setMessage(e.getMessage());
        standardError.setPath(servletRequest.getRequestURI());

        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> databaseException(DatabaseException e, HttpServletRequest servletRequest) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError standardError = new StandardError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(status.value());
        standardError.setError("Database Exception!");
        standardError.setMessage(e.getMessage());
        standardError.setPath(servletRequest.getRequestURI());

        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(PageablePropertyException.class)
    public ResponseEntity<StandardError> databaseException(PageablePropertyException e, HttpServletRequest servletRequest) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError standardError = new StandardError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(status.value());
        standardError.setError("Property invalid!");
        standardError.setMessage(e.getMessage());
        standardError.setPath(servletRequest.getRequestURI());

        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> methodValidationException(MethodArgumentNotValidException e, HttpServletRequest servletRequest) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError validationError = new ValidationError();
        validationError.setTimestamp(Instant.now());
        validationError.setStatus(status.value());
        validationError.setError("validation exception!");
        validationError.setMessage(e.getMessage());
        validationError.setPath(servletRequest.getRequestURI());

        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            validationError.addError(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(status).body(validationError);
    }


}

package com.discordshopping.controller;

import com.discordshopping.exception.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler({
            InvalidUUIDException.class,
            IllegalArgumentException.class,
            ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> invalidHandler(Throwable ex) {
        ErrorResponse response = ErrorResponse.builder(ex, BAD_REQUEST, ex.getMessage()).build();

        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler({
            NotFoundException.class})
    public ResponseEntity<ErrorResponse> notFoundExceptionHandler(Throwable ex) {
        ErrorResponse response = ErrorResponse.builder(ex, NOT_FOUND, ex.getMessage()).build();

        return new  ResponseEntity<>(response, NOT_FOUND);
    }

    @ExceptionHandler(InternalTechnicalErrorException.class)
    public ResponseEntity<ErrorResponse> internalExceptionHandler(Throwable ex) {
        ErrorResponse response = ErrorResponse.builder(ex, INTERNAL_SERVER_ERROR, ex.getMessage()).build();

        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }
}

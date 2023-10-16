package com.discordshopping.controller;

import com.discordshopping.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler({
            InvalidUUIDException.class,
            IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> invalidUUIDHandler(Throwable ex) {
        ErrorResponse response = ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage()).build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            NotFoundException.class})
    public ResponseEntity<ErrorResponse> notFoundExceptionHandler(Throwable ex) {
        ErrorResponse response = ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage()).build();

        return ResponseEntity.ofNullable(response);
    }
}

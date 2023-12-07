package com.discordshopping.controller;

import com.discordshopping.exception.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler({
            NullPointerException.class,
            InvalidUUIDException.class,
            IllegalArgumentException.class,
            InvalidCurrencyException.class,
            ConstraintViolationException.class,
            NumberFormatException.class,
            AlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> invalidHandler(Throwable ex) {
        ErrorResponse response = ErrorResponse.builder(ex, BAD_REQUEST, ex.getMessage()).build();

        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(NOT_FOUND)
    public String handleException(Exception e, Model model) {
        System.out.println(e.getMessage());
        return "redirect:/";
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

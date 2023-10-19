package com.discordshopping.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String e) {
        super(e);
    }
}

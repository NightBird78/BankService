package com.discordshopping.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String e) {
        super(e);
    }

    public NotFoundException(Throwable e) {
        super(e);
    }
}

package com.sefujo.common.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    private final String field;

    public EmailAlreadyExistsException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}

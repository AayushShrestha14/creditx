package com.sb.solutions.core.exception.handler;

public class Violation {

    private final String field;
    private final String message;
    private final Object rejectedValue;

    public Violation(String field, Object rejectedValue, String message) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public String getMessage() {
        return message;
    }
}

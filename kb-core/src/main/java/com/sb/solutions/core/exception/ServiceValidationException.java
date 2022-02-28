package com.sb.solutions.core.exception;

import java.util.List;

import com.sb.solutions.core.exception.handler.Violation;

public class ServiceValidationException extends RuntimeException {

    private List<Violation> violations;

    public ServiceValidationException(String message) {
        super(message);
    }

    public ServiceValidationException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ServiceValidationException(String message,
        List<Violation> violations) {
        super(message);
        this.violations = violations;
    }


    public void setViolations(List<Violation> violations) {
        this.violations = violations;
    }

    public List<Violation> getViolations() {
        return violations;
    }
}

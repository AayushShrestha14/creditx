package com.sb.solutions.core.exception;

public class InvalidTransactionRequest extends Exception {

    public InvalidTransactionRequest(String message) {
        super(message);
    }

    public InvalidTransactionRequest(String message, Throwable cause) {
        super(message, cause);
    }
}

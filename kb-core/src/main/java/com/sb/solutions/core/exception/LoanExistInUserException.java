package com.sb.solutions.core.exception;

/**
 * @author : Rujan Maharjan on  3/7/2021
 **/
public class LoanExistInUserException extends RuntimeException {

    private Object object;

    public LoanExistInUserException(String message,
        Object object) {
        super(message);
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}

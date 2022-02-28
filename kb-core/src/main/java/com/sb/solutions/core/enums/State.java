package com.sb.solutions.core.enums;

/**
 * @author Amulya Shrestha on 24/12/2019
 **/
public enum State {
    BLACKLISTED("Blacklisted"), SUSPENDED("Suspended");

    private final String value;

    State(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

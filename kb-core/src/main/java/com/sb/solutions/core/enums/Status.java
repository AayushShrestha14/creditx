package com.sb.solutions.core.enums;

/**
 * @author Sunil Babu Shrestha on 12/27/2018
 */
public enum Status {
    INACTIVE("Inactive"), ACTIVE("Active"), DELETED("Deleted"),LOCKED("Locked");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

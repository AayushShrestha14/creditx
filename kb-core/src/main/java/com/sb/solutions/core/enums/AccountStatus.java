package com.sb.solutions.core.enums;

public enum AccountStatus {
    NEW_REQUEST("New Request"), APPROVAL("Approved"), REJECTED("Rejected");

    private final String value;

    AccountStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

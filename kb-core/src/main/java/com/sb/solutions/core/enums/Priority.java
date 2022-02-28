package com.sb.solutions.core.enums;

public enum Priority {
    HIGH("High"), MEDIUM("Medium"), LOW("low");
    private final String value;

    Priority(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

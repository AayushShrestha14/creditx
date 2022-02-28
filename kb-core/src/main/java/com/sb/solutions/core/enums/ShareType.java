package com.sb.solutions.core.enums;

public enum ShareType {
    ORDINARY("ORDINARY"), PROMOTER("PROMOTER");

    private final String value;

    ShareType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

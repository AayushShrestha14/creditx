package com.sb.solutions.core.enums;

/**
 * @author Mohammad Hussain
 * created on 12/14/2020
 */
public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHERS("Others");
    private final String value;

    Gender(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

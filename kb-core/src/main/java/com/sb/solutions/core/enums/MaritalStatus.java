package com.sb.solutions.core.enums;

/**
 * @author Mohammad Hussain
 * created on 12/14/2020
 */
public enum MaritalStatus {

    SINGLE("Single"),
    MARRIED("Married"),
    SEPARATED("Separated"),
    DIVORCED("Divorced");

    private final String value;

    MaritalStatus(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return value;
    }
}

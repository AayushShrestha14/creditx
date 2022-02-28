package com.sb.solutions.api.customer.enums;

/**
 * @author : Rujan Maharjan on  8/10/2020
 **/
public enum CustomerType {
    INDIVIDUAL("individual"), INSTITUTION("institution");

    private final String value;

    CustomerType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

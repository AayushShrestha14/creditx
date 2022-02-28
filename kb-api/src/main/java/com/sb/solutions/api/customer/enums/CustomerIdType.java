package com.sb.solutions.api.customer.enums;

/**
 * @author : Rujan Maharjan on  8/10/2020
 **/
public enum CustomerIdType {
    CITIZENSHIP("citizenship"), PAN("pan");

    private final String value;

    CustomerIdType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

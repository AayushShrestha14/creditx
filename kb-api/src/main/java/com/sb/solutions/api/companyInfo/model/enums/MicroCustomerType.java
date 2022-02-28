package com.sb.solutions.api.companyInfo.model.enums;

public enum MicroCustomerType {
    DIRECT("Direct Customer"),INDIRECT("Indirect Customer");
    private final String value;
    MicroCustomerType(String s) {
        this.value = s;
    }

    public String getValue() {
        return this.value;
    }
}

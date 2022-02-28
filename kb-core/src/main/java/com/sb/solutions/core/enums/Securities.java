package com.sb.solutions.core.enums;

public enum Securities {
    LAND_SECURITY("Land Security"),
    BUILDING_SECURITY("Building Security"),
    VEHICLE_SECURITY("Vehicle Security"),
    PROPERTY_AND_MACHINERY_SECURITY("Property and Machinery Security"),
    FIXED_DEPOSIT_RECEIPT("Fixed Deposit Receipt"),
    PUBLIC_SHARE("Public Share"),
    EDUCATION_CERTIFICATE("Education Certificate"),
    MARKETABLE_SECURITIES("Marketable Securities"),
    GOLD_AND_SILVER("Gold & Silver"),
    PERSONAL_GUARANTOR("Personal Guarantor"),
    PROMOTER_SHARE("Promoter Share"),
    CORPORATE("Corporate");

    private final String value;

    Securities(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

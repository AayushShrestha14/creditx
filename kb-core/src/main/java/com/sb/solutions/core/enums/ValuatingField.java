package com.sb.solutions.core.enums;

/**
 * @author Elvin Shrestha on 1/12/20
 */
public enum ValuatingField {
    LAND("Land"),
    VEHICLE("Vehicle"),
    LAND_BUILDING("Land & Building");

    private final String value;

    ValuatingField(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

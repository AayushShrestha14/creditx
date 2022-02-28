package com.sb.solutions.core.enums;

/**
 * @author Elvin Shrestha on 1/22/2020
 */
public enum NepaliTemplateType {
    HIRE_PURCHASE_KARJA_NIBEDAN("Hire Purchase Karja Nibedan"),
    HIRE_PURCHASE_KARJA_BIKE("Hire Purchase Karja Bike"),
    AABEDAK_FAMILY_BIBARAN("Aabedak Family Bibaran"),
    JAMANI_BASEKO("Jamani Baseko");

    private final String value;

    NepaliTemplateType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

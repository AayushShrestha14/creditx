package com.sb.solutions.api.customerActivity.enums;

/**
 * @author : Rujan Maharjan on  9/19/2020
 **/
public enum ActivityType {
    MANUAL("MANUAL"),
    SCHEDULE("SCHEDULE");

    private final String value;

    ActivityType(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return value;
    }
}

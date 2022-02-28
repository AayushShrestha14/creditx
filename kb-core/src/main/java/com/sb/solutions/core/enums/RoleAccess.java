package com.sb.solutions.core.enums;

/**
 * @author Rujan Maharjan on 6/19/2019
 */
public enum RoleAccess {
    SPECIFIC("Specific"), OWN("Own"), ALL("All");
    private final String value;

    RoleAccess(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

package com.sb.solutions.core.enums;

/**
 * @author Rujan Maharjan on 6/12/2019
 */
public enum RoleType {

    MAKER("Maker"), APPROVAL("Approval"), COMMITTEE("Committee"),CAD_ADMIN("Cad Admin"),
    ADMIN("Admin"), CAD_SUPERVISOR("Cad Supervisor"),CAD_LEGAL("Cad Legal");

    private final String value;

    RoleType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

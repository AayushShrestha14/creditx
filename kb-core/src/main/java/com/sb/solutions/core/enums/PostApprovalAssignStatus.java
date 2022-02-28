package com.sb.solutions.core.enums;

/**
 * @author : Rujan Maharjan on  11/9/2020
 **/
public enum PostApprovalAssignStatus {
    NOT_ASSIGNED("not assigned"),
    ASSIGNED("assigned");

    private final String value;

    PostApprovalAssignStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

package com.sb.solutions.api.approvallimit.emuns;

public enum LoanApprovalType {

    INDIVIDUAL("individual"), INSTITUTION("institution"), BOTH_TYPE("both");

    private final String value;

    LoanApprovalType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}

package com.sb.solutions.api.loanConfig.enums;

public enum LoanNature {
    Revolving("Revolving"), Terminating("Terminating");

    private final String value;

    LoanNature(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}


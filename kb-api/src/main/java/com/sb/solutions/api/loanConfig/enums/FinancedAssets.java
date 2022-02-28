package com.sb.solutions.api.loanConfig.enums;

public enum FinancedAssets {
    WorkingCapital("WorkingCapital"), FixedAssets("FixedAssets");

    private final String value;

    FinancedAssets(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

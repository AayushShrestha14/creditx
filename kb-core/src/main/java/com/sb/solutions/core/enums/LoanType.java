package com.sb.solutions.core.enums;

/**
 * @author Sunil Babu Shrestha on 5/24/2019
 */
public enum LoanType {

    NEW_LOAN("New Loan"),
    RENEWED_LOAN("Renewed Loan"),
    CLOSURE_LOAN("Closure Loan"),
    ENHANCED_LOAN("Enhanced Loan"),
    PARTIAL_SETTLEMENT_LOAN("Partial Settlement Loan"),
    FULL_SETTLEMENT_LOAN("Full Settlement Loan"),
    RENEW_WITH_ENHANCEMENT("Renew with Enhancement");

    private final String value;

    LoanType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

package com.sb.solutions.api.approvallimit.emuns;

public enum LoanCategory {

    WORKING_CAPITAL_LOAN("Working Capital CustomerLoan"),
    BRIDGE_GAP_LOAN("Bridge Gap CustomerLoan"),
    TERM_LOAN("Term CustomerLoan"),
    SHARE_LOAN("Share CustomerLoan"),
    HIRE_PURCHASE_LOAN("Hire Purchase CustomerLoan"),
    PERSONAL_LOAN("Personal CustomerLoan"),
    DEPRIVED_LOAN("Deprived CustomerLoan"),
    HOME_LOAN("Home CustomerLoan"),
    LOAN_AGAINST_BOND("CustomerLoan Against Bond"),
    BIKE_HP_LOAN("Bike HP CustomerLoan"),
    FIXED_DEPOSIT_LOAN("Fixed Deposit CustomerLoan"),
    CONSUMER_FINANCE_LOAN("Consumer Finance CustomerLoan"),
    MEMO("Memo");

    private final String value;

    private LoanCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

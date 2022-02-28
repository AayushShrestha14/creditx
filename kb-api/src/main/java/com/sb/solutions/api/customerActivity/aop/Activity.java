package com.sb.solutions.api.customerActivity.aop;

/**
 * @author : Rujan Maharjan on  9/16/2020
 **/
public enum Activity {
    CUSTOMER_UPDATE("Customer Update"),
    INSURANCE_UPDATE("Insurance Update"),
    SITE_VISIT_UPDATE("Site Visit Update"),
    GUARANTOR_UPDATE("Guarantor Update"),
    FINANCIAL_UPDATE("Financial Update"),
    SECURITY("Security Update"),
    SHARE_SECURITY("Share Security Update"),
    CUSTOMER_GROUP_UPDATE("Customer Group Update"),
    LOAN_APPROVED("Loan Approved"),
    LOAN_UPDATE("Loan Update"),
    CRG_UPDATE("CRG Update"),
    CRG_ALPHA_UPDATE("CRG ALPHA Update"),
    CRG_GAMMA_UPDATE("CRG GAMMA Update"),
    CICL_UPDATE("CICL Update"),
    LOAN_TRANSFER("Loan Transfer"),
    DELETE_LOAN("Delete Loan"),
    RE_INITIATE_LOAN("Re-Initiate Loan"),
    LOAN_REJECT("Loan Rejected"),
    LOAN_CLOSED("Loan Closed");


    private final String value;

    Activity(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return value;
    }


}

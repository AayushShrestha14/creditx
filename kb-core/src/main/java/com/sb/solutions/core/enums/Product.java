package com.sb.solutions.core.enums;

/**
 * @author Rujan Maharjan on 6/24/2019
 */
public enum Product {

    DMS("Dms"),
    MEMO("Memo"),
    ACCOUNT("Account"),
    ELIGIBILITY("Eligibility"),
    LAS("Las"),
    OFFER_LETTER("Offer");

    private final String value;

    Product(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

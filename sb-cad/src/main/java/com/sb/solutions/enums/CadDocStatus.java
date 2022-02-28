package com.sb.solutions.enums;

/**
 * @author : Rujan Maharjan on  12/30/2020
 **/
public enum CadDocStatus {
    OFFER_PENDING("Offer Pending"),
    LEGAL_PENDING("Legal Pending"),
    DISBURSEMENT_PENDING("Disbursement Pending"),
    OFFER_APPROVED("Offer Approved"),
    LEGAL_APPROVED("Legal Approved"),
    DISBURSEMENT_APPROVED("Disbursement Approved");

    private final String value;

    CadDocStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

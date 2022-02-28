package com.sb.solutions.enums;

/**
 * @author : Rujan Maharjan on  12/1/2020
 **/
public enum CADDocumentType {
    OFFER_LETTER("Offer Letter"),
    LEGAL_DOCUMENT("Legal Document"),
    DISBURSEMENT("Disbursement");

    private final String value;

    CADDocumentType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

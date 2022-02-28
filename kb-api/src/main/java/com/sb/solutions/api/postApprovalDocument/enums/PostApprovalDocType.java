package com.sb.solutions.api.postApprovalDocument.enums;

/**
 * @author : Rujan Maharjan on  11/25/2020
 **/
public enum PostApprovalDocType {
    OFFER_LETTER("Offer Letter"),
    LEGAL_DOCUMENT("Legal Document"),
    DISBURSEMENT("Disbursement");

    private final String value;

    PostApprovalDocType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

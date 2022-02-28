package com.sb.solutions.enums;

/**
 * @author : Rujan Maharjan on  1/4/2021
 **/
public enum CADDocAction {
    DRAFT("Draft"),
    FORWARD("Forwarded"),
    BACKWARD("Backward"),
    REJECT("Reject"),
    CLOSED("Close"),
    TRANSFER("Transferred"),
    NOTED("Noted"),
    PULLED("Pulled"),
    BACKWARD_TO_COMMITTEE("Backward To Committee"),
    ASSIGNED("Assigned"),
    OFFER_APPROVED("Offer Approved"),
    LEGAL_APPROVED("Legal Approved"),
    DISBURSEMENT_APPROVED("Disbursement Approved");


    private final String value;

    CADDocAction(String value) {
        this.value = value;
    }
}

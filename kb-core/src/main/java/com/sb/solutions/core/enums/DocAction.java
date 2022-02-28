package com.sb.solutions.core.enums;

/**
 * @author Sunil Babu Shrestha on 5/24/2019
 * <p>
 * Collections of Action that can be performed on Loan Document
 */
public enum DocAction {
    DRAFT("Draft"),
    FORWARD("Forwarded"),
    BACKWARD("Backward"),
    APPROVED("Approved"),
    REJECT("Reject"),
    CLOSED("Close"),
    TRANSFER("Transferred"),
    NOTED("Noted"),
    PULLED("Pulled"),
    BACKWARD_TO_COMMITTEE("Backward To Committee"),
    ASSIGNED("Assigned"),
    CHANGE_LOAN("Change loan"),
    RE_INITIATE("Re Initiated");

    private final String value;

    DocAction(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return value;
    }

}

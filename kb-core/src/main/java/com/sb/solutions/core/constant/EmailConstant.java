package com.sb.solutions.core.constant;

import java.util.Map;

import com.google.common.collect.ImmutableMap;


/**
 * @author Sunil Babu Shrestha on 7/18/2019
 */
public final class EmailConstant {

    public static final Map<Template, String> MAIL = ImmutableMap.<Template, String>builder()
        .put(Template.RESET_PASSWORD, "/mail/password-reset.html")
        .put(Template.RESET_PASSWORD_SUCCESS, "/mail/password-reset-success.html")
        .put(Template.ELIGIBILITY_ELIGIBLE, "/mail/eligible.html")
        .put(Template.ACCOUNT_OPENING_THANK_YOU, "/mail/account-opening-thank-you.html")
        .put(Template.ONE_TIME_PASSWORD, "/mail/customer-otp.html")
        .put(Template.TEST, "/mail/email-test.html")
        .put(Template.ACCOUNT_OPENING_ACCEPT, "/mail/account-opening-registration-accept")
        .put(Template.ELIGIBILITY_APPROVE, "/mail/eligible-approve")
        .put(Template.INSURANCE_EXPIRY_MAKER, "/mail/insurance-expiry")
        .put(Template.INSURANCE_EXPIRY_CLIENT, "/mail/insurance-expiry-client")
        .put(Template.ACCOUNT_OPENING_BRANCH_NOTIFICATION, "/mail/account-opening-branch-notification")
        .put(Template.ELIGIBILITY_REQUEST, "/mail/eligible-request.html")
        .build();

    private EmailConstant() {
    }


    public enum Template {
        RESET_PASSWORD("Reset Password !!"),
        RESET_PASSWORD_SUCCESS("Password Reset Successful !!"),
        ELIGIBILITY_ELIGIBLE("You are Eligible !!"),
        ELIGIBILITY_APPROVE("Thank You !! Loan request has been approved."),
        ACCOUNT_OPENING_THANK_YOU("Thank You !! Account Opening request is received."),
        ONE_TIME_PASSWORD("One Time Password"),
        TEST("Email Configuration Test"),
        ACCOUNT_OPENING_ACCEPT("Thank You !! Account Opening request has been accepted."),
        INSURANCE_EXPIRY_MAKER("Your Insurance will be expired soon !!!"),
        INSURANCE_EXPIRY_CLIENT("Your Insurance will be expired soon !!!"),
        ACCOUNT_OPENING_BRANCH_NOTIFICATION("New Request for Account Opening"),
        ELIGIBILITY_REQUEST("Loan Request Sent");

        private String subject;

        Template(String subject) {
            this.subject = subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String get() {
            return this.subject;
        }
    }
}

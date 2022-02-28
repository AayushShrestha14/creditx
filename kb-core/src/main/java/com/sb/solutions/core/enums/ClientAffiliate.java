package com.sb.solutions.core.enums;

/**
 * Constant List of possible Business Type of the Company
 */
public enum ClientAffiliate {

    MEGA("mega");

    private String ClientAffiliate;

    ClientAffiliate(String s) {
        this.ClientAffiliate = s;
    }

    @Override
    public String toString() {
        return this.ClientAffiliate;
    }

}

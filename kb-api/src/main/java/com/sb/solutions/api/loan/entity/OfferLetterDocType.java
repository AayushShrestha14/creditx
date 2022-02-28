package com.sb.solutions.api.loan.entity;

/**
 * @author Bibash Bogati on 11/19/2020
 */
public enum OfferLetterDocType {
    DRAFT("Draft"), SIGNED("Signed");

    OfferLetterDocType(String value) {
        this.value = value;
    }

    private final String value;

    @Override
    public String toString() {
        return value;
    }

    }

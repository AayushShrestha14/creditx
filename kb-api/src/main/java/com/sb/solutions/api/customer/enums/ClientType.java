package com.sb.solutions.api.customer.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sunil Babu Shrestha on 11/12/2020
 */
public enum ClientType {
    CORPORATE("Corporate"),
    INFRASTRUCTURE_AND_PROJECT("Infrastructure & Project"),
    BUSINESS_DEVELOPMENT("Business Development"),
    MID_MARKET("Mid Market"),
    MICRO_FINANCIAL_SERVICES("Micro Financial Services"),
    CONSUMER_FINANCE("Consumer Finance"),
    DEPRIVED_SECTOR("Deprived Sector"),
    SMALL_BUSINESS_FINANCIAL_SERVICES("Small Business Financial Services"),
    HR_DEPARTMENT("HR Department");

    private final String value;

    ClientType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Map<ClientType, String> getClientType() {
        Map<ClientType, String> map;
        map = new HashMap<>();

        for (ClientType clientType : ClientType.values()) {
            map.put(clientType, clientType.toString());
        }
        return map;
    }
}

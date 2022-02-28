package com.sb.solutions.core.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

public class BankUtils {

    public static String AFFILIATED_ID;

    public static Map<String, Object> map = new HashMap<>();

    @Value("${bank.affiliateId}")
    public void setAffiliatedId(String affiliatedId) {
        AFFILIATED_ID = affiliatedId;
        map.put("AFFILIATED_ID", affiliatedId);
    }

    public static Map<String, Object> getMap() {
        return map;
    }
}

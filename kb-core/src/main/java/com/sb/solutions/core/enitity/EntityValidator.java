package com.sb.solutions.core.enitity;

import org.springframework.data.util.Pair;

/**
 * @author Sunil Babu Shrestha on 7/3/2020
 */
@FunctionalInterface
public interface EntityValidator {

    /*
    Pair Left - validation (true/false)
    Pair Right - validation message
     */
    Pair<Boolean, String> valid();

    /**
     * @return
     */
    default boolean isValid() {
        Pair<Boolean, String> pair = valid();
        return pair.getFirst();
    }

    /**
     * @return
     */
    default String getValidationMsg() {
        Pair<Boolean, String> pair = valid();
        return pair.getSecond();
    }
}

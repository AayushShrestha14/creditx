package com.sb.solutions.core.utils;

import java.util.Arrays;

/**
 * @author : Rujan Maharjan on  9/19/2020
 **/
public class EnumUtils {

    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }

}

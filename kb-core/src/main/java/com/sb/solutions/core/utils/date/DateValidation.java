package com.sb.solutions.core.utils.date;

import java.util.Date;

public class DateValidation {

    public static boolean isPreviousDate(Date date) {
        return date.before(new Date());
    }

    public static boolean isFutureDate(Date date) {
        return date.after(new Date());
    }
}

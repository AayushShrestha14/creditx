package com.sb.solutions.core.utils.date;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Rujan Maharjan on 6/7/2019
 */
public class EnglishDate {

    public static final String[] WEEK_DAYS = {"Sunday", "Monday", "Tuesday",
        "Wednesday", "Thursday", "Friday", "Saturday"};

    private Date englishDate;

    public EnglishDate(Date englishDate) {
        this.englishDate = englishDate;
    }


    public int getYear() {
        return englishDate.getYear();
    }

    public String getMonthAsText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM");
        return dateFormat.format(englishDate);
    }

    public int getMonth() {
        return englishDate.getMonth();
    }

    public int getDate() {
        return englishDate.getDay();
    }

    public String toString() {
        Calendar c = Calendar.getInstance();
        c.setTime(englishDate);
        return c.get(Calendar.YEAR) + " / " + getMonth() + " / "
            + getDate() + " " + WEEK_DAYS[c.get(Calendar.DAY_OF_WEEK)];
    }

    public static int getWeekIndex(String weekDay) {
        return Arrays.asList(WEEK_DAYS).indexOf(weekDay) + 1;
    }
}

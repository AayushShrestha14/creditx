package com.sb.solutions.core.utils.date;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.NoArgsConstructor;

/**
 * @author Rujan Maharjan on 6/7/2019
 */

@NoArgsConstructor
public class Converter {

    //Change this when you get more previous database.
    public static final int START_ENGLISH_YEAR = 1923;
    public static final int START_ENGLISH_MONTH = 4;
    public static final int START_ENGLISH_DAY = 13;

    //Change this to equivalent nepali start date
    public static final int START_NEPALI_YEAR = 1980;
    public static final int START_NEPALI_MONTH = 1;
    public static final int START_NEPALI_DAY = 1;

    /*
        English units start with e
        Nepali units start with n
    */
    public NepaliDate getNepaliDate(int eYear, int eMonth, int eDay) {
        int nDay = START_NEPALI_DAY;
        int nMonth = START_NEPALI_MONTH;
        int nYear = START_NEPALI_YEAR;

        final Calendar end = Calendar.getInstance();
        end.set(eYear, eMonth, eDay);

        LocalDate startDate = LocalDate
            .of(START_ENGLISH_YEAR, START_ENGLISH_MONTH, START_ENGLISH_DAY);
        LocalDate endDate = LocalDate.of(eYear, eMonth, eDay);

        int tempDeltaDays = (int) ChronoUnit.DAYS.between(startDate, endDate);
        NepaliDate nepaliDate = new NepaliDate();
        for (int i = 0; i < tempDeltaDays; i++) {
            if (nDay < nepaliDate.getDaysOf(nYear, nMonth)) {
                nDay++;
            } else if (nMonth < 12) {
                nDay = 1;
                nMonth++;
            } else if (nMonth == 12) {
                nYear++;
                nMonth = 1;
                nDay = 1;
            }
        }

        String weekDay = "" + end.get(Calendar.DAY_OF_WEEK);

        return new NepaliDate(nYear, nMonth, nDay, weekDay);
    }

    public List<NepaliDate> getFullNepaliMonthOf(int eYear, int eMonth, int eDay) {

        NepaliDate temp = getNepaliDate(eYear, eMonth, eDay);
        int nDay = temp.getGatey();
        int nYear = temp.getSaal();
        int nMonth = temp.getMahina();
        String nWeekDay = temp.getBaar();
        List<NepaliDate> nepaliDates = new ArrayList<NepaliDate>();

        int weekIndex = com.sb.solutions.core.utils.date.EnglishDate
            .getWeekIndex(nWeekDay);
        for (int i = nDay; i > 0; i--) {
            nepaliDates.add(new NepaliDate(nYear, nMonth, i,
                com.sb.solutions.core.utils.date.EnglishDate.WEEK_DAYS[weekIndex
                    - 1]));
            if (weekIndex > 1) {
                weekIndex--;
            } else {
                weekIndex = 7;
            }
        }
        weekIndex = com.sb.solutions.core.utils.date.EnglishDate
            .getWeekIndex(nWeekDay);
        if (weekIndex < 7) {
            weekIndex++;
        } else {
            weekIndex = 1;
        }
        for (int i = nDay + 1; i <= temp.getDaysOf(nYear, nMonth); i++) {
            nepaliDates.add(new NepaliDate(nYear, nMonth, i,
                com.sb.solutions.core.utils.date.EnglishDate.WEEK_DAYS[weekIndex
                    - 1]));
            if (weekIndex < 7) {
                weekIndex++;
            } else {
                weekIndex = 1;
            }
        }

        nepaliDates.sort((p1, p2) -> p1.getGatey() - p2.getGatey());

        return nepaliDates;
    }

}

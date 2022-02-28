package com.sb.solutions.core.utils.date;

import static com.sb.solutions.core.utils.date.Converter.START_ENGLISH_DAY;
import static com.sb.solutions.core.utils.date.Converter.START_ENGLISH_MONTH;
import static com.sb.solutions.core.utils.date.Converter.START_ENGLISH_YEAR;
import static com.sb.solutions.core.utils.date.Converter.START_NEPALI_DAY;
import static com.sb.solutions.core.utils.date.Converter.START_NEPALI_MONTH;
import static com.sb.solutions.core.utils.date.Converter.START_NEPALI_YEAR;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rujan Maharjan on 6/7/2019
 */

public class NepaliDate {

    private int mahina;
    private int gatey;
    private int saal;
    private String baar;


    public static final String[] MONTHS = {"Baisakh", "Jestha", "Ashar", "Shrawan",
            "Bhadra", "Ashoj", "Kartik", "Mangsir",
            "Poush", "Magh", "Falgun", "Chaitra"};

    private int[][] numberOfDaysPerYear = {
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            {31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            {31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            {31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {30, 32, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            {31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {30, 32, 31, 32, 31, 31, 29, 30, 29, 30, 29, 31},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            {31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            {31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            {31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            {31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30},
            {31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            {31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            {31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}
    };

    public NepaliDate() {

    }

    public NepaliDate(int saal, int mahina, int gatey, String baar) {
        this.mahina = mahina;
        this.gatey = gatey;
        this.saal = saal;
        this.baar = baar;
    }

    public int getMahina() {
        return mahina;
    }

    public int getGatey() {
        return gatey;
    }

    public int getSaal() {
        return saal;
    }

    public String getBaar() {
        return baar;
    }

    public String getMahinaInWords() {
        return MONTHS[mahina - 1];
    }

    public int getDaysOf(int year, int month) {
        return numberOfDaysPerYear[year
                - START_NEPALI_YEAR]
                [month - 1];
    }

    public String toString() {
        return saal + " / " + mahina + " / " + gatey + " " + baar;
    }

    public List calendarMap() {
        int length = numberOfDaysPerYear.length;
        List yearList = new ArrayList();
        int startYear = START_NEPALI_YEAR;
        for (int i = 0; i < length; i++) {
            Map map = new LinkedHashMap();
            map.put("year", startYear + i);
            List monthList = new ArrayList();
            for (int j = 0; j < 12; j++) {
                Map monthMap = new LinkedHashMap();
                monthMap.put("month", MONTHS[j]);
                monthMap.put("numberOfDaysPerMonth", numberOfDaysPerYear[i][j]);
                monthMap.put("englishStartDateOfNepMonth", getNepaliDay(startYear + i, j + 1, 1));
                monthList.add(monthMap);
            }
            map.put("monthMap", monthList);
            yearList.add(map);
        }
        return yearList;
    }


    public Map<String, Object> getNepaliDay(int nYear, int nMonth, int nDay) {
        Map<String, Object> map = new LinkedHashMap<>();
        int lDay = START_NEPALI_DAY;
        int lMonth = START_NEPALI_MONTH;
        int lYear = START_NEPALI_YEAR;
        long deltaDays = 0;
        boolean isReached = false;

        while (!isReached) {
            if (nYear == lYear && nMonth == lMonth && nDay == lDay) {
                isReached = true;
                deltaDays--;
            }
            deltaDays++;
            if (lDay < this.getDaysOf(lYear, lMonth)) {
                lDay++;
            } else if (lMonth < 12) {
                lDay = 1;
                lMonth++;
            } else if (lMonth == 12) {
                lYear++;
                lMonth = 1;
                lDay = 1;
            }
        }

        LocalDate localDate = LocalDate
                .of(START_ENGLISH_YEAR, START_ENGLISH_MONTH, START_ENGLISH_DAY);
        map.put("date", localDate.plusDays(deltaDays));
        map.put("day", localDate.plusDays(deltaDays).getDayOfMonth());
        map.put("month", localDate.plusDays(deltaDays).getMonth());
        map.put("year", localDate.plusDays(deltaDays).getYear());
        String day = localDate.plusDays(deltaDays).getDayOfWeek().toString();
        map.put("startDay", day.substring(0, 1).toUpperCase() + day.substring(1, 2).toLowerCase());
        return map;

    }

    public static Map<String, Object> engAndNepStartDate() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("startEnglishDay", START_ENGLISH_DAY);
        map.put("startEnglishMonth", START_ENGLISH_MONTH);
        map.put("startEnglishYear", START_ENGLISH_YEAR);
        map.put("startNepaliDay", START_NEPALI_DAY);
        map.put("startNepaliMonth", START_NEPALI_MONTH);
        map.put("startNepaliYear", START_NEPALI_YEAR);
        return map;
    }
}

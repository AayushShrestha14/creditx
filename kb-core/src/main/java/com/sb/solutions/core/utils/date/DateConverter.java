package com.sb.solutions.core.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Rujan Maharjan on 6/7/2019 date Format for conversion= dd-mm-yyyy format
 */
@Component
public class DateConverter {

    private static final Logger logger = LoggerFactory.getLogger(DateConverter.class);

    public Map<Object, Object> getNepaliDate(@PathVariable String id) throws ParseException {
        Map<Object, Object> map = new HashMap<>();
        try {
            try {
                String[] date = id.split("-");
                try {
                    int day = Integer.parseInt(date[0]);
                    int months = Integer.parseInt(date[1]);
                    int year = Integer.parseInt(date[2]);
                    Converter converter = new Converter();
                    NepaliDate nepaliDate = converter.getNepaliDate(year, months, day);
                    map.put("nepDate",
                        nepaliDate.getGatey() + "-" + nepaliDate.getMahina() + "-" + nepaliDate
                            .getSaal());
                    map.put("nepDateYMD",
                        nepaliDate.getSaal() + "-" + nepaliDate.getMahina() + "-" + nepaliDate
                            .getGatey());
                    map.put("nepDateFormat",
                        nepaliDate.getGatey() + " " + nepaliDate.getMahinaInWords() + ", "
                            + nepaliDate.getSaal());
                    map.put("nepGate", nepaliDate.getGatey());
                    map.put("nepDateMonth", nepaliDate.getMahinaInWords());
                    map.put("nepDateYear", nepaliDate.getSaal());
                    map.put("baar", nepaliDate.getBaar());

                    return map;
                } catch (Exception e) {
                    map.put("error", e.getLocalizedMessage());
                    return map;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                map.put("error", e);
                return map;
            }
        } catch (Exception e) {
            map.put("error", e.getMessage());
            return map;
        }
    }

    /*public Map<Object, Object> getEnglishDate(@PathVariable String id) throws ParseException {
        Map<Object, Object> map = new HashMap<>();
        try {
            try {
                String[] Date = id.split("-");
                try {
                    int day = Integer.parseInt(Date[0]);
                    int months = Integer.parseInt(Date[1]);
                    int year = Integer.parseInt(Date[2]);
                    Converter converter = new Converter();
                    com.sb.solutions.core.utils.NepaliDateConvertor.EnglishDate englishDate
                     = converter.getEnglishDate(year, months, day);
                    map.put("engDate", englishDate.getDate() + "-" + englishDate.getMonth()
                    + "-" + englishDate.getYear());
                    return map;
                } catch (Exception e) {
                    map.put("error", e.getLocalizedMessage());
                    return map;
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                map.put("error", e.getMessage());
                return map;
            }

        } catch (IllegalFieldValueException e) {
            map.put("error", e.getMessage());
            return map;
        }
    }

    public DateFormat getNumberDaysInNepaliMonth(@RequestParam("year") String year,
    @RequestParam("month") String month) throws ParseException {
        Map<Object, Object> map = new HashMap<>();

        int mnth = Integer.parseInt(nepMnthMapper().get(month));
        NepaliDate np = new NepaliDate();
        int noOfDays = np.getDaysOf(Integer.parseInt(year), mnth);
        map.put("startNepDate", 1);
        map.put("endNepDate", noOfDays);
        map.put("yearNep", year);
        map.put("monthNep", month);
        //convertor
        Converter converter = new Converter();
        EnglishDate startEnDate = converter.getEnglishDate(Integer.parseInt(year), mnth, 1);
        EnglishDate endEnDate = converter.getEnglishDate(Integer.parseInt(year), mnth, noOfDays);
        map.put("startEnDate", startEnDate);
        map.put("endEnDate", endEnDate);
        DateFormat dateFormat = new DateFormat();
        String startDate = startEnDate.getYear() + "-" + startEnDate.getMonth()
        + "-" + startEnDate.getDate();
        String endDate = endEnDate.getYear() + "-" + endEnDate.getMonth()
        + "-" + endEnDate.getDate();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date start = df.parse(startDate);
        Date end = df.parse(endDate);

        dateFormat.setMonth(month);
        dateFormat.setYear(year);
        dateFormat.setStartDate(start);
        dateFormat.setEndDate(end);
        return dateFormat;

    }*/

    public Map<String, String> nepMnthMapper() {
        Map<String, String> map = new HashMap<>();
        map.put("Baishakh", "01");
        map.put("Jestha", "02");
        map.put("Asar", "03");
        map.put("Shrawan", "04");
        map.put("Bhadau", "05");
        map.put("Aswin", "06");
        map.put("Kartik", "07");
        map.put("Mansir", "08");
        map.put("Poush", "09");
        map.put("Magh", "10");
        map.put("Falgun", "11");
        map.put("Chaitra", "12");

        return map;

    }

    public Map<String, String> engMnthMapper() {
        Map<String, String> map = new HashMap<>();
        map.put("January", "01");
        map.put("February", "02");
        map.put("March", "03");
        map.put("April", "04");
        map.put("May", "05");
        map.put("June", "06");
        map.put("July", "07");
        map.put("August", "08");
        map.put("September", "09");
        map.put("October", "10");
        map.put("November", "11");
        map.put("December", "12");

        return map;
    }

    public Map<Object, Object> getCurrentNepaliDate() throws ParseException {
        Date d = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = simpleDateFormat.format(d);
        return new DateConverter().getNepaliDate(formattedDate);
    }

    public Map<Object, Object> getCurrentEnglishDate() {
        Calendar cal = Calendar.getInstance();
        Map<Object, Object> map = new HashMap<>();
        map.put("engDateMonth",
            cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));

        //map.put("year",new SimpleDateFormat("yyyy").format())
        return map;
    }

    public String convertDate(String date, String calendarType) {
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            SimpleDateFormat d = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = d.format(date1);
            if (calendarType.equalsIgnoreCase("AD")) {
                return new SimpleDateFormat("dd MMM, yyyy")
                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            } else if (calendarType.equalsIgnoreCase("BS")) {
                return new DateConverter().getNepaliDate(formattedDate).get("nepDateFormat")
                    .toString();
            }

        } catch (Exception e) {
            logger.error("Error while converting date", e);
        }
        return null;
    }

}

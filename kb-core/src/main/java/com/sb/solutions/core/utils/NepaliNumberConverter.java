package com.sb.solutions.core.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NepaliNumberConverter {

    private static final Logger logger = LoggerFactory.getLogger(NepaliNumberConverter.class);

    private NepaliNumberConverter() {
    }

    public static String englishNumberToNepali(String number) {

        String nepNumber = "";

        try {
            BigDecimal value = new BigDecimal(number);
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.ROOT);
            currencyFormatter.setMaximumFractionDigits(5);
            String formattedCurrency = currencyFormatter.format(value);

            List<Character> characterList = formattedCurrency.chars().mapToObj(e -> (char) e)
                .collect(Collectors.toList());

            for (Character c : characterList) {
                if (Character.isDigit(c)) {
                    nepNumber = nepNumber + mapper(c.toString());
                } else {
                    nepNumber = nepNumber + c.toString();
                }
            }
            nepNumber = nepNumber.replace("¤", "");
        } catch (Exception e) {
            logger.error(
                "Error while converting {}",
                e.getMessage());
        }

        return nepNumber;
    }

    private static String mapper(String num) {
        Map<String, String> map = new HashMap<>();
        map.put("1", "१");
        map.put("2", "२");
        map.put("3", "३");
        map.put("4", "४");
        map.put("5", "५");
        map.put("6", "६");
        map.put("7", "७");
        map.put("8", "८");
        map.put("9", "९");
        map.put("0", "0");
        map.put(".", ".");
        map.put(" ", "");

        return map.get(num);
    }

    public static void main(String[] args) {
        System.out.println(englishNumberToNepali("11178789898054311123.44398"));

    }
}

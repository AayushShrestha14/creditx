package com.sb.solutions.core.utils;

public final class NumberToWordsConverter {


    public static final String[] units = {"", "One", "Two", "Three", "Four",
        "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve",
        "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
        "Eighteen", "Nineteen"};
    public static final String[] tens = {
        "",        // 0
        "",        // 1
        "Twenty",    // 2
        "Thirty",    // 3
        "Forty",    // 4
        "Fifty",    // 5
        "Sixty",    // 6
        "Seventy",    // 7
        "Eighty",    // 8
        "Ninety"    // 9
    };

    private NumberToWordsConverter() {
    }

    public static String calculateAmountInWords(String amount) {
        if (amount.contains(".")) {

            String[] parts = amount.split("\\.");

            String amountInWords = convert(Long.parseLong(parts[0]));
            if (parts[1] != null && Double.parseDouble(parts[1]) != 0.0) {
                String amountAfterPoint = convert(Long.parseLong(parts[1].substring(0, 2)));
                amountInWords = amountInWords + " And " + amountAfterPoint + " Paisa";
            }

            return amountInWords;
        } else {
            return convert(Long.parseLong(amount));
        }
    }

    public static String convert(final Long n) {
        if (n < 0) {
            return "minus " + convert(-n);
        }

        if (n < 20) {
            return units[n.intValue()];
        }

        if (n < 100) {
            return tens[n.intValue() / 10] + ((n % 10 != 0) ? " " : "") + units[n.intValue() % 10];
        }

        if (n < 1000) {
            return units[n.intValue() / 100] + " Hundred" + ((n % 100 != 0) ? " " : "") + convert(n % 100);
        }

        if (n < 1000000) {
            return convert(n / 1000) + " Thousand" + ((n % 1000 != 0) ? " " : "") + convert(n % 1000);
        }

        if (n < 1000000000) {
            return convert(n / 1000000) + " Million" + ((n % 1000000 != 0) ? " " : "") + convert(n % 1000000);
        }

        return convert(n / 1000000000) + " Billion"  + ((n % 1000000000 != 0) ? " " : "") + convert(n % 1000000000);
    }

    public static void main(String[] args) {
        System.out.println(convert(9900000000000l));
    }

}

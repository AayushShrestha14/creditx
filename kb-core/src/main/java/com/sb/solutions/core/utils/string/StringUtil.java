package com.sb.solutions.core.utils.string;

import java.security.SecureRandom;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;

import com.sb.solutions.core.exception.ServiceValidationException;

public class StringUtil {

    private static final String COMBO_SEED = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMBER_SEED = "0123456789";
    private static final String NULL_CHECK = "value cannot be null";
    private static final String WHITESPACE = " ";

    private static SecureRandom random = new SecureRandom();

    public static String generate(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(COMBO_SEED.charAt(random.nextInt(COMBO_SEED.length())));
        }
        return sb.toString();
    }

    public static String generateNumber(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(NUMBER_SEED.charAt(random.nextInt(NUMBER_SEED.length())));
        }
        return sb.toString();
    }

    public static String getAcronym(final String sentence, final String seperator) {
        String acronym = Stream.of(sentence.split(seperator))
            .map(s -> s.substring(0, 1))
            .collect(Collectors.joining());

        return acronym;
    }

    public static String getStringWithoutWhiteSpaceAndWithCapitalize(String string) {
        if (StringUtils.isBlank(string)) {
            throw new ServiceValidationException(NULL_CHECK);
        }
        String[] nameArray = string.split(" ");
        String finalName = "";
        for (String s : nameArray) {
            finalName = finalName + StringUtils.capitalize(s);
        }
        finalName = StringUtils.deleteWhitespace(finalName);
        return finalName.trim();
    }

    public static String textFormatter(String string) {
        if (!StringUtils.isBlank(string)) {
            String finalName = string.replaceAll("\\s+", WHITESPACE);
            int pos = 0;
            boolean capitalize = true;
            StringBuilder sb = new StringBuilder(finalName.toLowerCase());
            while (pos < sb.length()) {
                if (sb.charAt(pos) == '.') {
                    capitalize = true;
                } else if (capitalize && !Character.isWhitespace(sb.charAt(pos))) {
                    sb.setCharAt(pos, Character.toUpperCase(sb.charAt(pos)));
                    capitalize = false;
                }
                pos++;
            }
            return sb.toString().trim();
        }
        return null;
    }

    public static String getStringWithWhiteSpaceAndWithAllFirstLetterCapitalize(String string) {
        if (!StringUtils.isBlank(string)) {
            StringTokenizer st = new StringTokenizer(string, WHITESPACE);
            StringBuilder sb = new StringBuilder();

            while (st.hasMoreElements()) {
                sb.append(StringUtils.capitalize(st.nextElement().toString().toLowerCase()))
                    .append(WHITESPACE);
            }

            return sb.toString().trim();
        }
        return null;
    }


}

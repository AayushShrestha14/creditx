package com.sb.solutions.core.utils.base64Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Utility {

    public static String getFileExtension(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    public static String validateImageExtension(String fileName) {
        String extension = getFileExtension(fileName);
        if (extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg")) {
            return "SUCCESS";
        } else {
            return "UNSUCCESS";
        }
    }

    public String generateRandomNumber() {
        String uniqueId = UUID.randomUUID().toString();
        String uid = uniqueId.substring(0, Math.min(uniqueId.length(), 8));
        return uid + getDateAndTime();
    }

    public String getDateAndTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hhssssyyyyMMddmm");
        return sdf.format(d);
    }

    public String fiveDigitRand() {
        Random r = new Random(System.currentTimeMillis());
        return String.valueOf(((1 + r.nextInt(2)) * 10000 + r.nextInt(10000)));
    }

    public Date strToDate(String s) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(s);
        return date;
    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    public String randomStringGenerator(int length) {
        final String saltValue = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * saltValue.length());
            salt.append(saltValue.charAt(index));
        }
        return salt.toString();

    }
}

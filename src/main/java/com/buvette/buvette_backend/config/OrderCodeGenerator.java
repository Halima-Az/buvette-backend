package com.buvette.buvette_backend.config;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OrderCodeGenerator {

    private static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom random = new SecureRandom();

    public static String generate() {
        String datePart = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyMMdd"));

        return datePart + "-" + randomPart(4);
    }

    private static String randomPart(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}

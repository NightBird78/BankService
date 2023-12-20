package com.discordshopping.util;

import com.github.javafaker.Faker;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;


public class Util {

    public static UUID encode(String name) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return UUID.nameUUIDFromBytes(md.digest(name.getBytes()));
    }

    /**
     * Create any data.<p>
     * use faker, cause it`s easier
     */

    public static String createBankIdentifier() {
        Faker faker = new Faker();
        String number = faker.regexify("[0-9]{16}");

        String primaryCode = "IDBA";

        return primaryCode + number;
    }

    public static boolean isValidEmail(String email) {
        return email.matches("[a-z0-9_.]+@[a-z]+\\.[a-z]+$");
    }

    public static String check(String sum) {

        int num = Integer.parseInt(sum);
        num = num / 1000;
        return Double.toString((double) num / 10);
    }

    public static double check(Double sum) {

        sum = (double) (int) (sum / 1000);
        return sum / 10;
    }

    public static BigDecimal round(BigDecimal num, int afterComma) {

        String[] split = num.toString().split("\\.");

        return getBigDecimal(afterComma, split);
    }

    public static BigDecimal round(Double num, int afterComma) {

        String[] split = Double.toString(num).split("\\.");

        return getBigDecimal(afterComma, split);
    }

    @NotNull
    private static BigDecimal getBigDecimal(int afterComma, String[] split) {
        if (afterComma == 0) {
            return BigDecimal.valueOf(Long.parseLong(split[0]));
        }
        if (split[1].length() > afterComma) {
            split[1] = split[1].substring(0, afterComma);
        }

        return BigDecimal.valueOf(Long.parseLong(split[0]))
                .add(BigDecimal.valueOf(Double.parseDouble("." + split[1])));
    }
}
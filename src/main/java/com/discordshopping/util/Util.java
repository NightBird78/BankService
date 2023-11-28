package com.discordshopping.util;

import com.github.javafaker.Faker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Util {

    public static UUID encode(String name) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA256");

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

    public static boolean isValidIDBA(String idba) {
        return idba.matches("idba[0-9]{16}$");
    }

    public static boolean isValidEmail(String email) {
        return email.matches("[a-z0-9_.]+@[a-z]+\\.[a-z]+$");
    }

    public static String check(String sum) {

        int num = Integer.parseInt(sum);
        num = num / 1000;
        return Double.toString((double) num/10);
    }

    public static double check(Double sum) {

        sum = (double) (int) (sum / 1000);
        return sum/10;
    }
}
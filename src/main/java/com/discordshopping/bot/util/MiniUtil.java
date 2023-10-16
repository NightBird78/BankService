package com.discordshopping.bot.util;

import com.github.javafaker.Faker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MiniUtil {

    public static UUID encode(String name) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA256");

        return UUID.nameUUIDFromBytes(md.digest(name.getBytes()));
    }

    public static boolean isValidUUID(String id){
        Pattern pattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
        Matcher matcher = pattern.matcher(id);

        if(matcher.find()) {
            return matcher.group().equals(id);
        }
        return false;
    }

    public static String createBankIdentifier(){
        Faker faker = new Faker();
        String number = faker.regexify("[0-9]{16}");

        String primaryCode = "IDBA";

        return primaryCode+number;
    }
}
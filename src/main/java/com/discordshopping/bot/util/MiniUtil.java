package com.discordshopping.bot.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;


public class MiniUtil {

    public static UUID encode(String name) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA256");

        return UUID.nameUUIDFromBytes(md.digest(name.getBytes()));
    }
}
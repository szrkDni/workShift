package com.project.springjavafx.javaFXApp.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordHandler {

    public static String encrypt(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }
    public static boolean verify(String password, String hash) {

        return encrypt(password).equals(hash);
    }
}

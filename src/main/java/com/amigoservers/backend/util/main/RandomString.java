package com.amigoservers.backend.util.main;

public class RandomString {
    public static String random(int length) {
        String chars = "qwertzuiopasdfghjklyxcvbnm0987654321";
        StringBuilder randomString = new StringBuilder();
        while (length > 0) {
            int random = (int) (Math.random() * chars.length());
            randomString.append(chars.charAt(random));
            length--;
        }
        return randomString.toString();
    }
}

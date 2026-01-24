package com.example.cheffy.utils;

import java.util.regex.Pattern;

public final class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +         // at least 1 digit
            "(?=.*[a-z])" +         // at least 1 lower case letter
            "(?=.*[A-Z])" +         // at least 1 upper case letter
            "(?=.*[@#$%^&+=_])" +    // at least 1 special character
            "(?=\\S+$)" +           // no white spaces
            ".{8,}" +               // at least 8 characters
            "$"
    );

    private ValidationUtils() { }

    public static boolean isInValidEmail(String email) {
        return email == null || !EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}

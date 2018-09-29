package com.iyans.utility;

import android.util.Patterns;

public class Validation {
    public static final boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static final boolean isValidRadius(String str) {
        if (str != null) {
            try {
                if (str.length() <= 0 || Integer.parseInt(str) <= 0) {
                    return false;
                }
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    public static final boolean isValidString(String str) {
        if (str == null || str.length() <= 0) {
            return false;
        }
        return true;
    }

    public static final boolean isValidDescription(String str) {
        if (str == null || str.length() <= 10) {
            return false;
        }
        return true;
    }

    public static final boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static final boolean isValidPhoneNumber(CharSequence target) {
        if (target.length() < 10) {
            return false;
        }
        return Patterns.PHONE.matcher(target).matches();
    }

    public static boolean isValidPassword(String password) {
        if (password.length() >= 6 && !password.contains(" ")) {
            return true;
        }
        return false;
    }

    public static boolean isValidLoginPassword(String password) {
        if (password.length() < 1) {
            return false;
        }
        return true;
    }
}

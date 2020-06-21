package com.vinuthana.vinvidyaadmin.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KISHAN on 18-09-17.
 */

public class Validation {
    public String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public String PHONE_PATTERN = "\\d{10}";

    public boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public boolean isValidMobile(String phone) {
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
        //return android.util.Patterns.PHONE.matcher(strPhoneNumber).matches();
    }
}

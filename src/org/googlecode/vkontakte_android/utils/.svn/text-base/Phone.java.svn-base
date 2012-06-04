package org.googlecode.vkontakte_android.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Phone {
    public static String formatPhoneNumber(String number) {
        number = number.replaceAll("[-()]*", "");
        Pattern pattern = Pattern.compile("(\\+?\\d*).*");
        Matcher matcher = pattern.matcher(number);
        return matcher.find() ? matcher.group(1) : null;
    }
}

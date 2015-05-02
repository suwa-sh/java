package me.suwash.util;

import java.util.Locale;

public class TestLocale {

    public static void main(String[] args) {
        Locale locale = Locale.JAPAN;
        System.out.println(locale.toString());
        System.out.println(locale.getCountry());
        System.out.println(locale.getISO3Country());
        System.out.println(locale.getLanguage());
        System.out.println(locale.getScript());
    }

}

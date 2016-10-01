package me.suwash.util;

import java.util.Locale;

import org.junit.Test;

public class LocaleTest {

    @Test
    public void testPrint() {
        Locale locale = Locale.JAPAN;
        System.out.println("toString       : " + locale.toString());
        System.out.println("getCountry     : " + locale.getCountry());
        System.out.println("getISO3Country : " + locale.getISO3Country());
        System.out.println("getLanguage    : " + locale.getLanguage());
        System.out.println("getScript      : " + locale.getScript());
    }

}

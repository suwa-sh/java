package me.suwash.util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import me.suwash.test.DefaultTestWatcher;
import me.suwash.util.exception.UtilException;
import me.suwash.util.test.UtilTestWatcher;

import org.junit.Rule;
import org.junit.Test;

@lombok.extern.slf4j.Slf4j
public class DateUtilsTest {

    @Rule
    public DefaultTestWatcher watcher = new UtilTestWatcher();

    @Test
    public void testToDate() {
        Date date1 = null;
        Date date2 = null;

        // ミリ秒あり
        date1 = DateUtils.toDate("2015-03-13 10:20:30,400");
        date2 = DateUtils.toDate("2015-03-13 10:20:30.399");
        assertTrue(date1.compareTo(date2) >= 0);

        // ISO8601
        date1 = DateUtils.toDate("2015-03-13T10:20:30.232+0900:");
        date2 = DateUtils.toDate("2015/03/13 10:20:30.232");
        assertTrue(date1.compareTo(date2) == 0);

        // apache access log
        date1 = DateUtils.toDate("[13/Mar/2015:10:20:30 +0900]");
        // ※1時間進んでいるtimezoneでの同時刻なのでこっちが進んでいる
        date2 = DateUtils.toDate("[13/Mar/2015:10:20:30 +0800]");
        assertTrue(date1.compareTo(date2) < 0);

        try {
            date1 = DateUtils.toDate("2015/02/30");
            fail("UtilException をthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
        }

        try {
            date1 = DateUtils.toDate("This is not DATE.");
            fail("UtilException をthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
        }
    }

    @Test
    public void testToCalendar() {
        Calendar cal1 = null;
        Calendar cal2 = null;

        // ミリ秒あり
        cal1 = DateUtils.toCalendar("2015-03-13 10:20:30,400");
        cal2 = DateUtils.toCalendar("2015-03-13 10:20:30.399");
        assertTrue(cal1.compareTo(cal2) > 0);

        // ISO8601
        cal1 = DateUtils.toCalendar("2015-03-13T10:20:30.123+0900:");
        cal2 = DateUtils.toCalendar("2015-03-13T10:20:30,123+0900");
        assertTrue(cal1.compareTo(cal2) == 0);
        cal2 = DateUtils.toCalendar("2015-03-13T10:20:30+0900");
        assertTrue(cal1.compareTo(cal2) > 0);
        cal2 = DateUtils.toCalendar("2015/03/13 10:20:30.123");
        assertTrue(cal1.compareTo(cal2) == 0);
        // ISO8601 で 秒なし
        cal2 = DateUtils.toCalendar("2015-03-13T10:20+0900");
        assertTrue(cal1.compareTo(cal2) > 0);
        log.debug(cal2.getTime().toString());

        // apache access log
        cal1 = DateUtils.toCalendar("[13/Mar/2015:10:20:30 +0900]");
        // ※1時間進んでいるtimezoneでの同時刻なのでこっちが進んでいる
        cal2 = DateUtils.toCalendar("[13/Mar/2015:09:20:30 +0800]");
        assertTrue(cal1.compareTo(cal2) == 0);
        cal2 = DateUtils.toCalendar("2015/03/13 10:20:30");
        assertTrue(cal1.compareTo(cal2) == 0);
        cal2 = DateUtils.toCalendar("2015/03/13 10:20");
        assertTrue(cal1.compareTo(cal2) > 0);

        // apache access log + ミリ秒
        try {
            cal2 = DateUtils.toCalendar("[13/Mar/2015:10:20:30.000 +0900]");
            fail("UtilException をthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
        }
        // apache access log で -区切り
        try {
            cal2 = DateUtils.toCalendar("[13-Mar-2015:10:20:30 +0900]");
            fail("UtilException をthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
        }


        try {
            cal1 = DateUtils.toCalendar("2015/02/30");
            fail("UtilException をthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
        }

        try {
            cal1 = DateUtils.toCalendar("This is not DATE.");
            fail("UtilException をthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
        }
    }

}

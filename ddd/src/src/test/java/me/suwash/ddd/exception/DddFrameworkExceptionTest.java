package me.suwash.ddd.exception;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import me.suwash.util.exception.UtilException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DddFrameworkExceptionTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public final void testDddFrameworkExceptionString() {
        String messageId = "check.notNull";
        String expected = "[check.notNull]{0} が設定されていません。";

        try {
            throw new DddFrameworkException(messageId);
        } catch(DddFrameworkException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    public final void testDddFrameworkExceptionString_存在しないメッセージIDの場合() {
        String messageId = "NotExistMessageId";
        String expected = "[check.undefined]message_source_ja.properties.NotExistMessageId が定義されていません。";

        try {
            throw new DddFrameworkException(messageId);
        } catch(DddFrameworkException e) {
            try {
                e.getMessage();
            } catch (UtilException e2) {
                assertEquals("メッセージ取得時にメッセージIDが未定義の旨のエラーがthrowされること", expected, e2.getMessage());

            }
        }
    }

    @Test
    public final void testDddFrameworkExceptionString_メッセージ取得前に指定ロケールを変更した場合() {
        String messageId = "check.notNull";
        Locale locale = Locale.US;
        String expected = "[check.notNull]{0} is mandatory.";

        try {
            throw new DddFrameworkException(messageId);
        } catch(DddFrameworkException e) {
            e.setLocale(locale);
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    public final void testDddFrameworkExceptionString_メッセージ取得前にシステムロケールを変更した場合() {
        String messageId = "check.notNull";
        Locale defaultLocale = Locale.getDefault();
        Locale locale = Locale.US;
        String expected = "[check.notNull]{0} is mandatory.";

        try {
            throw new DddFrameworkException(messageId);
        } catch(DddFrameworkException e) {
            Locale.setDefault(locale);
            String actual = e.getMessage();
            Locale.setDefault(defaultLocale);

            assertEquals(expected, actual);
        }
    }

    @Test
    public final void testDddFrameworkExceptionStringObjectArray() {
        String messageId = "check.notNull";
        Object[] messsageArgs = new Object[]{"messageId"};
        String expected = "[check.notNull]メッセージID が設定されていません。";

        try {
            throw new DddFrameworkException(messageId, messsageArgs);
        } catch(DddFrameworkException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    public final void testDddFrameworkExceptionStringThrowable() {
        String messageId = "check.notNull";
        String expected = "[check.notNull]{0} が設定されていません。";

        String causeMessage = "元となる例外のメッセージ";
        String causeExpected = causeMessage;

        try {
            throw new RuntimeException(causeMessage);
        } catch(RuntimeException cause) {
            try {
                throw new DddFrameworkException(messageId, cause);
            } catch(DddFrameworkException e) {
                assertEquals(expected, e.getMessage());
                assertEquals(causeExpected, e.getCause().getMessage());
            }
        }
    }

    @Test
    public final void testDddFrameworkExceptionStringObjectArrayThrowable() {
        String messageId = "check.notNull";
        Object[] messageArgs = new Object[] {"messageId"};
        String expected = "[check.notNull]メッセージID が設定されていません。";

        String causeMessage = "元となる例外のメッセージ";
        String causeExpected = causeMessage;

        try {
            throw new RuntimeException(causeMessage);
        } catch(RuntimeException cause) {
            try {
                throw new DddFrameworkException(messageId, messageArgs, cause);
            } catch(DddFrameworkException e) {
                assertEquals(expected, e.getMessage());
                assertEquals(causeExpected, e.getCause().getMessage());
            }
        }
    }

    @Test
    public final void testDddFrameworkExceptionStringLocale() {
        String messageId = "check.notNull";
        Locale locale = Locale.US;
        String expected = "[check.notNull]{0} is mandatory.";

        try {
            throw new DddFrameworkException(messageId, locale);
        } catch(DddFrameworkException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    public final void testDddFrameworkExceptionStringObjectArrayLocale() {
        String messageId = "check.notNull";
        Object[] messsageArgs = new Object[]{"messageId"};
        Locale locale = Locale.US;
        String expected = "[check.notNull]MessageId is mandatory.";

        try {
            throw new DddFrameworkException(messageId, messsageArgs, locale);
        } catch(DddFrameworkException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    public final void testDddFrameworkExceptionStringLocaleThrowable() {
        String messageId = "check.notNull";
        String expected = "[check.notNull]{0} is mandatory.";
        Locale locale = Locale.US;

        String causeMessage = "元となる例外のメッセージ";
        String causeExpected = causeMessage;

        try {
            throw new RuntimeException(causeMessage);
        } catch(RuntimeException cause) {
            try {
                throw new DddFrameworkException(messageId, locale, cause);
            } catch(DddFrameworkException e) {
                assertEquals(expected, e.getMessage());
                assertEquals(causeExpected, e.getCause().getMessage());
            }
        }
    }

    @Test
    public final void testDddFrameworkExceptionStringObjectArrayLocaleThrowable() {
        String messageId = "check.notNull";
        Object[] messsageArgs = new Object[]{"messageId"};
        Locale locale = Locale.US;
        String expected = "[check.notNull]MessageId is mandatory.";

        String causeMessage = "元となる例外のメッセージ";
        String causeExpected = causeMessage;

        try {
            throw new RuntimeException(causeMessage);
        } catch(RuntimeException cause) {
            try {
                throw new DddFrameworkException(messageId, messsageArgs, locale, cause);
            } catch(DddFrameworkException e) {
                assertEquals(expected, e.getMessage());
                assertEquals(causeExpected, e.getCause().getMessage());
            }
        }
    }

}

package me.suwash.util.exception;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UtilExceptionTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @SuppressWarnings("deprecation")
    @Test
    public void test() {
        String CAUSE_MESSAGE = "This is TEST-CAUSE.";

        String messageId = null;
        Object[] messageArgs = null;
        Locale locale = Locale.US;
        Throwable cause = new RuntimeException(CAUSE_MESSAGE);
        UtilException ex = null;

        //--------------------------------------------------
        // 存在しないIDの場合
        //--------------------------------------------------
        try {
            messageId = "NotExistId";
            ex = new UtilException(messageId);
            ex.getMessage();
            fail("IllegalArgumentException が発生すること。");
        } catch (IllegalArgumentException e) {
        }

        //--------------------------------------------------
        // メッセージ引数なし
        //--------------------------------------------------
        messageId = "CODE0001";

        ex = new UtilException(messageId);
        assertNull("ロケールが取得できないこと", ex.getLocale());
        assertEquals("メッセージIDが取得できること", messageId, ex.getMessageId());
        assertTrue("メッセージ定義をそのまま返却すること", ex.getMessage().contains("サンプルのメッセージです。"));
        assertNull("causeが取得できないこと", ex.getCause());
        assertNull("メッセージ引数が取得できないこと", ex.getMessageArgs());

        // cause指定
        ex = new UtilException(messageId, cause);
        assertNull("ロケールが取得できないこと", ex.getLocale());
        assertEquals("メッセージIDが取得できること", messageId, ex.getMessageId());
        assertTrue("メッセージ定義をそのまま返却すること", ex.getMessage().contains("サンプルのメッセージです。"));
        assertEquals("causeが取得できること", CAUSE_MESSAGE, ex.getCause().getMessage());
        assertNull("メッセージ引数が取得できないこと", ex.getMessageArgs());

        // ロケール指定
        ex = new UtilException(messageId, locale);
        assertEquals("指定ロケールが取得できること", locale, ex.getLocale());
        assertEquals("メッセージIDが取得できること", messageId, ex.getMessageId());
        assertTrue("メッセージ定義をそのまま返却すること", ex.getMessage().contains("this is sample message."));
        assertNull("causeが取得できないこと", ex.getCause());
        assertNull("メッセージ引数が取得できないこと", ex.getMessageArgs());

        // ロケール、cause指定
        ex = new UtilException(messageId, locale, cause);
        assertEquals("指定ロケールが取得できること", locale, ex.getLocale());
        assertEquals("メッセージIDが取得できること", messageId, ex.getMessageId());
        assertTrue("メッセージ定義をそのまま返却すること", ex.getMessage().contains("this is sample message."));
        assertEquals("causeが取得できること", CAUSE_MESSAGE, ex.getCause().getMessage());
        assertNull("メッセージ引数が取得できないこと", ex.getMessageArgs());

        //--------------------------------------------------
        // メッセージ引数あり
        //--------------------------------------------------
        messageId = "CODE0002";
        messageArgs = new Object[] { "functionCategory.function.SampleKbn", "functionCategory.function.SampleKbn.sample1", "functionCategory.function.SampleKbn.sample2" };

        ex = new UtilException(messageId, messageArgs);
        assertNull("ロケールが取得できないこと", ex.getLocale());
        assertEquals("メッセージIDが取得できること", messageId, ex.getMessageId());
        assertTrue("メッセージの定義に項目名を反映して返却すること", ex.getMessage().contains("サンプル区分の値は、サンプル1とサンプル2です。"));
        assertNull("causeが取得できないこと", ex.getCause());
        assertEquals("メッセージ引数が取得できること", messageArgs, ex.getMessageArgs());

        // cause指定
        ex = new UtilException(messageId, messageArgs, cause);
        assertNull("ロケールが取得できないこと", ex.getLocale());
        assertEquals("メッセージIDが取得できること", messageId, ex.getMessageId());
        assertTrue("メッセージの定義に項目名を反映して返却すること", ex.getMessage().contains("サンプル区分の値は、サンプル1とサンプル2です。"));
        assertEquals("causeが取得できること", CAUSE_MESSAGE, ex.getCause().getMessage());
        assertEquals("メッセージ引数が取得できること", messageArgs, ex.getMessageArgs());

        // ロケール指定
        ex = new UtilException(messageId, messageArgs, locale);
        assertEquals("指定ロケールが取得できること", locale, ex.getLocale());
        assertEquals("メッセージIDが取得できること", messageId, ex.getMessageId());
        assertTrue("メッセージの定義に項目名を反映して返却すること", ex.getMessage().contains("SampleClassification contains SampleONE and SampleTWO."));
        assertNull("causeが取得できないこと", ex.getCause());
        assertEquals("メッセージ引数が取得できること", messageArgs, ex.getMessageArgs());

        // ロケール、cause指定
        ex = new UtilException(messageId, messageArgs, locale, cause);
        assertEquals("指定ロケールが取得できること", locale, ex.getLocale());
        assertEquals("メッセージIDが取得できること", messageId, ex.getMessageId());
        assertTrue("メッセージの定義に項目名を反映して返却すること", ex.getMessage().contains("SampleClassification contains SampleONE and SampleTWO."));
        assertEquals("causeが取得できること", CAUSE_MESSAGE, ex.getCause().getMessage());
        assertEquals("メッセージ引数が取得できること", messageArgs, ex.getMessageArgs());

        // ロケール切り替え
        ex.setLocale(Locale.JAPAN);
        assertEquals("指定ロケールが取得できること", Locale.JAPAN, ex.getLocale());
        assertTrue("メッセージの定義に項目名を反映して返却すること", ex.getMessage().contains("サンプル区分の値は、サンプル1とサンプル2です。"));
    }

}

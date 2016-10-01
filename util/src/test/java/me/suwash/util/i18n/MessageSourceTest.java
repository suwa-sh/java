package me.suwash.util.i18n;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MessageSourceTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test(expected = IllegalArgumentException.class)
    public void testGetMessage_存在しないIDの場合() {
        MessageSource message = MessageSource.getInstance();
        String messageId = "NotExistId";

        // IllegalArgumentExceptionをthrowすること
        message.getMessage(messageId);
    }

    @Test
    public void testGetMessage_空文字の場合() {
        MessageSource message = MessageSource.getInstance();
        String messageId = StringUtils.EMPTY;
        try {
            message.getMessage(messageId);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("messageId is empty.", e.getMessage());
        }

        messageId = null;
        try {
            message.getMessage(messageId);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("messageId is empty.", e.getMessage());
        }
    }

    @Test
    public void testGetMessage_ロケール指定なし_引数なし_プレースホルダなしの場合() {
        MessageSource message = MessageSource.getInstance();
        String messageId = "CODE0001";

        assertEquals("メッセージ定義をそのまま返却すること", "サンプルのメッセージです。", message.getMessage(messageId));
    }

    @Test
    public void testGetMessage_ロケール指定なし_引数なし_プレースホルダありの場合() {
        MessageSource message = MessageSource.getInstance();
        String messageId = "CODE0002";

        assertEquals("メッセージの定義をそのまま返却すること", "{0}の値は、{1}と{2}です。", message.getMessage(messageId));
    }

    @Test
    public void testGetMessage_ロケール指定なし_引数あり_プレースホルダなしの場合() {
        MessageSource message = MessageSource.getInstance();
        String messageId = "CODE0001";
        Object[] messageArgs = new Object[] { "functionCategory.function.SampleKbn", "functionCategory.function.SampleKbn.sample1", "functionCategory.function.SampleKbn.sample2" };

        assertEquals("メッセージの定義をそのまま返却すること", "サンプルのメッセージです。", message.getMessage(messageId, messageArgs));
    }

    @Test
    public void testGetMessage_ロケール指定なし_引数あり_プレースホルダありの場合() {
        MessageSource message = MessageSource.getInstance();
        String messageId = "CODE0002";
        Object[] messageArgs = new Object[] { "functionCategory.function.SampleKbn", "functionCategory.function.SampleKbn.sample1", "functionCategory.function.SampleKbn.sample2" };

        assertEquals("メッセージの定義に項目名を反映して返却すること", "サンプル区分の値は、サンプル1とサンプル2です。", message.getMessage(messageId, messageArgs));
    }

    @Test
    public void testGetMessage_ロケール指定なし_引数あり_プレースホルダあり_存在しないDDIDの場合() {
        MessageSource message = MessageSource.getInstance();
        String messageId = "CODE0002";
        Object[] messageArgs = new Object[] { "NotExist1", "NotExist2" };

        assertEquals("メッセージの定義に項目名を反映して返却すること", "NotExist1の値は、NotExist2と{2}です。", message.getMessage(messageId, messageArgs));
    }

    @Test
    public void testGetMessage_ロケール指定なし_引数あり_プレースホルダなし_引数がアンマッチの場合() {
        MessageSource message = MessageSource.getInstance();
        String messageId = "CODE0002";

        assertEquals("メッセージ定義をそのまま返却すること", "{0}の値は、{1}と{2}です。", message.getMessage(messageId));
    }

    @Test
    public void testGetMessage_ロケール指定あり_引数なし_プレースホルダなしの場合() {
        MessageSource message = MessageSource.getInstance();
        String messageId = "CODE0001";
        Locale locale = Locale.US;

        assertEquals("メッセージ定義をそのまま返却すること", "this is sample message.", message.getMessage(messageId, locale));
    }

    @Test
    public void testGetMessage_ロケール指定あり_引数なし_プレースホルダありの場合() {
        MessageSource message = MessageSource.getInstance();
        String messageId = "CODE0002";
        Locale locale = Locale.US;

        assertEquals("メッセージの定義をそのまま返却すること", "{0} contains {1} and {2}.", message.getMessage(messageId, locale));
    }

    @Test
    public void testGetMessage_ロケール指定あり_引数あり_プレースホルダなしの場合() {
        MessageSource message = MessageSource.getInstance();
        String messageId = "CODE0001";
        Object[] messageArgs = new Object[] { "functionCategory.function.SampleKbn", "functionCategory.function.SampleKbn.sample1", "functionCategory.function.SampleKbn.sample2" };
        Locale locale = Locale.US;

        assertEquals("メッセージの定義をそのまま返却すること", "this is sample message.", message.getMessage(messageId, messageArgs, locale));
    }

    @Test
    public void testGetMessage_ロケール指定あり_引数あり_プレースホルダありの場合() {
        MessageSource message = MessageSource.getInstance();
        String messageId = "CODE0002";
        Object[] messageArgs = new Object[] { "functionCategory.function.SampleKbn", "functionCategory.function.SampleKbn.sample1", "functionCategory.function.SampleKbn.sample2" };
        Locale locale = Locale.US;

        assertEquals("メッセージの定義に項目名を反映して返却すること", "SampleClassification contains SampleONE and SampleTWO.", message.getMessage(messageId, messageArgs, locale));
    }

    @Test
    public void testGetMessage_ロケール指定あり_引数あり_プレースホルダあり_存在しないDDIDの場合() {
        MessageSource message = MessageSource.getInstance();
        String messageId = "CODE0002";
        Object[] messageArgs = new Object[] { "NotExist1", "NotExist2" };
        Locale locale = Locale.US;

        assertEquals("メッセージの定義に項目名を反映して返却すること", "NotExist1 contains NotExist2 and {2}.", message.getMessage(messageId, messageArgs, locale));
    }

    @Test
    public void testClearCache() throws IOException {
        MessageSource message = MessageSource.getInstance();
        String messageId = "CODE0002";
        Object[] messageArgs = new Object[] { "NotExist1", "NotExist2" };

        String expectBeforeReplace = "NotExist1の値は、NotExist2と{2}です。";
        String expectAfterReplace = expectBeforeReplace + "置換しました。";

        FileSystem fs = FileSystems.getDefault();
        Path mainPath = fs.getPath(this.getClass().getResource("/message_source_ja.properties").getPath());
        Path defaultPath = fs.getPath(this.getClass().getResource("/DEFAULT_message_source_ja.properties").getPath());
        Path testPath = fs.getPath(this.getClass().getResource("/TEST_message_source_ja.properties").getPath());

        // ファイル入れ替え前
        assertEquals("置換されていないこと", expectBeforeReplace, message.getMessage(messageId, messageArgs));
        // ファイル入れ替え
        Files.copy(testPath, mainPath, StandardCopyOption.REPLACE_EXISTING);

        // キャッシュクリア前
        assertEquals("置換されていないこと", expectBeforeReplace, message.getMessage(messageId, messageArgs));
        // キャッシュクリア
        message.clearCache();
        // キャッシュクリア後
        assertEquals("置換されていること", expectAfterReplace, message.getMessage(messageId, messageArgs));

        // ファイルを戻す
        Files.copy(defaultPath, mainPath, StandardCopyOption.REPLACE_EXISTING);
        message.clearCache();
        assertEquals("置換されていないこと", expectBeforeReplace, message.getMessage(messageId, messageArgs));
    }

}

package me.suwash.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import me.suwash.util.constant.UtilMessageConst;
import me.suwash.util.exception.UtilException;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

@lombok.extern.slf4j.Slf4j
public class FileUtilsTest {

    private static final String DIR_BASE = "src/test/scripts/util/" + FileUtilsTest.class.getSimpleName();
    private static final String CLASSPATH_BASE = "/ut/util/" + FileUtilsTest.class.getSimpleName();
    private static final String CHARSET = "utf8";

    @Rule
    public TestName name = new TestName();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        log.debug("■■■ " + FileUtilsTest.class.getName() + " ■■■");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        log.debug("■ " + name.getMethodName() + " - START");
        log.debug(RuntimeUtils.getMemoryInfo());
    }

    @After
    public void tearDown() throws Exception {
        log.debug(RuntimeUtils.getMemoryInfo());
        log.debug("■ " + name.getMethodName() + " - END");
    }

    @Test
    public void testReadCheck() {
        final String dir = DIR_BASE + "/" + name.getMethodName();
        final String existFilePath = dir + "/input.txt";
        final String notExistFilePath = dir + "/notexist.txt";

        String filePath;
        String charset;

        // 正常系
        filePath = existFilePath;
        charset = CHARSET;
        FileUtils.readCheck(filePath, charset);

        // パス＝空文字
        filePath = StringUtils.EMPTY;
        charset = CHARSET;
        try {
            FileUtils.readCheck(filePath, charset);
            fail("UtilExceptionをthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
        }

        // パス＝存在しない
        filePath = notExistFilePath;
        charset = CHARSET;
        try {
            FileUtils.readCheck(filePath, charset);
            fail("UtilExceptionをthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("存在チェックのメッセージが返されること", UtilMessageConst.CHECK_NOTEXIST, e.getMessageId());
        }

        // パス＝ディレクトリ
        filePath = dir;
        charset = CHARSET;
        try {
            FileUtils.readCheck(filePath, charset);
            fail("UtilExceptionをthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("fileチェックのメッセージが返されること", UtilMessageConst.FILE_CHECK, e.getMessageId());
        }

        // 文字コード＝空文字
        filePath = existFilePath;
        charset = StringUtils.EMPTY;
        try {
            FileUtils.readCheck(filePath, charset);
            fail("UtilExceptionをthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
        }

        // 文字コード＝エラー
        filePath = existFilePath;
        charset = "ERROR";
        try {
            FileUtils.readCheck(filePath, charset);
            fail("UtilExceptionをthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("unsupportのメッセージが返されること", UtilMessageConst.UNSUPPORTED_PATTERN, e.getMessageId());
        }
    }

    @Test
    public void testReadCheckByClasspath() {
        final String dir = CLASSPATH_BASE + "/" + name.getMethodName();
        final String existFilePath = dir + "/input.txt";
        final String notExistFilePath = dir + "/notexist.txt";

        String filePath;
        String charset;

        // 正常系
        filePath = existFilePath;
        charset = CHARSET;
        FileUtils.readCheckByClasspath(filePath, charset, FileUtilsTest.class);

        // パス＝空文字
        filePath = StringUtils.EMPTY;
        charset = CHARSET;
        try {
            FileUtils.readCheckByClasspath(filePath, charset, FileUtilsTest.class);
            fail("UtilExceptionをthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
        }

        // パス＝存在しない
        filePath = notExistFilePath;
        charset = CHARSET;
        try {
            FileUtils.readCheckByClasspath(filePath, charset, FileUtilsTest.class);
            fail("UtilExceptionをthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("存在チェックのメッセージが返されること", UtilMessageConst.CHECK_NOTEXIST, e.getMessageId());
        }

        // パス＝ディレクトリ
        filePath = dir;
        charset = CHARSET;
        try {
            FileUtils.readCheckByClasspath(filePath, charset, FileUtilsTest.class);
            fail("UtilExceptionをthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("fileチェックのメッセージが返されること", UtilMessageConst.FILE_CHECK, e.getMessageId());
        }

        // 文字コード＝空文字
        filePath = existFilePath;
        charset = StringUtils.EMPTY;
        try {
            FileUtils.readCheckByClasspath(filePath, charset, FileUtilsTest.class);
            fail("UtilExceptionをthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
        }

        // 文字コード＝エラー
        filePath = existFilePath;
        charset = "ERROR";
        try {
            FileUtils.readCheckByClasspath(filePath, charset, FileUtilsTest.class);
            fail("UtilExceptionをthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("unsupportのメッセージが返されること", UtilMessageConst.UNSUPPORTED_PATTERN, e.getMessageId());
        }
    }

    @Test
    public void testWriteCheck() {
        final String dir = DIR_BASE + "/" + name.getMethodName();
        final String existFilePath = dir + "/input.txt";

        String filePath;
        String charset;

        // 正常系
        filePath = existFilePath;
        charset = CHARSET;
        FileUtils.writeCheck(filePath, charset);

        // パス＝空文字
        filePath = StringUtils.EMPTY;
        charset = CHARSET;
        try {
            FileUtils.writeCheck(filePath, charset);
            fail("UtilExceptionをthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
        }

        // パス＝ディレクトリ
        filePath = dir;
        charset = CHARSET;
        try {
            FileUtils.writeCheck(filePath, charset);
            fail("UtilExceptionをthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("fileチェックのメッセージが返されること", UtilMessageConst.FILE_CHECK, e.getMessageId());
        }

        // 文字コード＝空文字
        filePath = existFilePath;
        charset = StringUtils.EMPTY;
        try {
            FileUtils.writeCheck(filePath, charset);
            fail("UtilExceptionをthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
        }

        // 文字コード＝エラー
        filePath = existFilePath;
        charset = "ERROR";
        try {
            FileUtils.writeCheck(filePath, charset);
            fail("UtilExceptionをthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("unsupportのメッセージが返されること", UtilMessageConst.UNSUPPORTED_PATTERN, e.getMessageId());
        }
    }

    @Test
    public void testMkdirs() {
        // 存在するディレクトリ
        assertTrue("存在するディレクトリの場合、trueを返すこと", FileUtils.mkdirs(DIR_BASE));

        // ディレクトリを削除
        final String dirPath = DIR_BASE + "/" + name.getMethodName();
        File dir = new File(dirPath);
        if (dir.exists()) {
            dir.delete();
        }

        // 存在しないディレクトリ
        assertTrue("存在するディレクトリの場合、trueを返すこと", FileUtils.mkdirs(dirPath));
    }

    @Test
    public void testSetupOverwrite() throws IOException {
        final String dir = DIR_BASE + "/" + name.getMethodName();
        final String existFilePath = dir + "/input.txt";
        final String notExistFilePath = dir + "/notExist.txt";
        final String notExistDirPath = dir + "/notExist";
        final String notExistDirAndFilePath = notExistDirPath + "/notExist.txt";

        String filePath;

        // ファイルが存在する場合
        File existFile = new File(existFilePath);
        if (! existFile.exists()) {
            FileUtils.mkdirs(existFile.getParent());
            existFile.createNewFile();
        }
        filePath = existFilePath;
        FileUtils.setupOverwrite(filePath);
        assertFalse("ファイルが削除されていること", existFile.exists());

        // 親ディレクトリは存在するが、ファイルは存在しない場合
        File notExistDir = new File(notExistDirPath);
        if (notExistDir.exists()) {
            notExistDir.delete();
        }
        filePath = notExistFilePath;
        FileUtils.setupOverwrite(filePath);
        assertFalse("ファイルが存在しないままであること", new File(filePath).exists());

        // 親ディレクトリも、ファイルも存在しない場合
        filePath = notExistDirAndFilePath;
        FileUtils.setupOverwrite(filePath);
        File targetFile = new File(filePath);
        assertTrue("ディレクトリが作成されていること", targetFile.getParentFile().exists());
        assertFalse("ファイルが存在しないままであること", new File(filePath).exists());
    }

}

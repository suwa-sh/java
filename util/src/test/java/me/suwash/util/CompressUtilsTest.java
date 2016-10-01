package me.suwash.util;

import static org.junit.Assert.*;

import java.io.File;

import me.suwash.util.constant.UtilMessageConst;
import me.suwash.util.exception.UtilException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

@lombok.extern.slf4j.Slf4j
public class CompressUtilsTest {

    private static final String DIR_BASE = "src/test/scripts/util/" + CompressUtilsTest.class.getSimpleName();
    private static final String DIR_INPUT = DIR_BASE + "/input";
//    private static final String DIR_EXPECT = DIR_INPUT;
    private static final String DIR_ACTUAL = DIR_BASE + "/actual";

    @Rule
    public TestName name = new TestName();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        log.debug("■■■ " + CompressUtilsTest.class.getName() + " ■■■");
        log.debug("-- Init actual dir --");
        FileUtils.rmdirs(DIR_ACTUAL);
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
    public void testCompressTarGz() {
        String targetDirPath;
        String outputFilePath;
        boolean isRelative;
        File outputFile;

        // 圧縮（相対パス出力）
        targetDirPath = DIR_INPUT;
        outputFilePath = DIR_ACTUAL + "/testComparessTarGz.tar.gz";
        isRelative = true;
        CompressUtils.compressTarGz(targetDirPath, outputFilePath, isRelative);
        outputFile = new File(outputFilePath);
        assertTrue("ファイルが存在すること", outputFile.isFile());
        // TODO uncompress対応までは、手動で確認

        // 圧縮（相対パス出力）
        targetDirPath = DIR_INPUT;
        outputFilePath = DIR_ACTUAL + "/testComparessTarGz_absPath.tar.gz";
        isRelative = false;
        CompressUtils.compressTarGz(targetDirPath, outputFilePath, isRelative);
        outputFile = new File(outputFilePath);
        assertTrue("ファイルが存在すること", outputFile.isFile());

        // 出力ファイルパスを指定しない場合
        targetDirPath = DIR_INPUT;
        outputFilePath = null;
        isRelative = false;
        CompressUtils.compressTarGz(targetDirPath, outputFilePath, isRelative);
        outputFile = new File(DIR_INPUT + ".tar.gz");
        assertTrue("ファイルが存在すること", outputFile.isFile());
        outputFile.delete();

        // 存在しないディレクトリの場合
        targetDirPath = DIR_INPUT + "/notExist";
        outputFilePath = null;
        isRelative = false;
        try {
            CompressUtils.compressTarGz(targetDirPath, outputFilePath, isRelative);
            fail("UtilException をthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("存在チェックのメッセージが返されること", UtilMessageConst.CHECK_NOTEXIST, e.getMessageId());
        }

    }

    @Test
    public void testCompressTar() {
        String targetDirPath;
        String outputFilePath;
        boolean isRelative;
        File outputFile;

        // 圧縮（相対パス出力）
        targetDirPath = DIR_INPUT;
        outputFilePath = DIR_ACTUAL + "/testComparessTar.tar";
        isRelative = true;
        CompressUtils.compressTar(targetDirPath, outputFilePath, isRelative);
        outputFile = new File(outputFilePath);
        assertTrue("ファイルが存在すること", outputFile.isFile());
        // TODO uncompress対応までは、手動で確認

        // 圧縮（相対パス出力）
        targetDirPath = DIR_INPUT;
        outputFilePath = DIR_ACTUAL + "/testComparessTar_absPath.tar";
        isRelative = false;
        CompressUtils.compressTar(targetDirPath, outputFilePath, isRelative);
        outputFile = new File(outputFilePath);
        assertTrue("ファイルが存在すること", outputFile.isFile());

        // 出力ファイルパスを指定しない場合
        targetDirPath = DIR_INPUT;
        outputFilePath = null;
        isRelative = false;
        CompressUtils.compressTar(targetDirPath, outputFilePath, isRelative);
        outputFile = new File(DIR_INPUT + ".tar");
        assertTrue("ファイルが存在すること", outputFile.isFile());
        outputFile.delete();

        // 存在しないディレクトリの場合
        targetDirPath = DIR_INPUT + "/notExist";
        outputFilePath = null;
        isRelative = false;
        try {
            CompressUtils.compressTar(targetDirPath, outputFilePath, isRelative);
            fail("UtilException をthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("存在チェックのメッセージが返されること", UtilMessageConst.CHECK_NOTEXIST, e.getMessageId());
        }

    }

    @Test
    public void testCompressGz() {
        String targetFilePath;
        String outputFilePath;
        File outputFile;

        // 圧縮
        targetFilePath = DIR_INPUT + "/input.csv";
        outputFilePath = DIR_ACTUAL + "/input.csv.gz";
        CompressUtils.compressGz(targetFilePath, outputFilePath);
        outputFile = new File(outputFilePath);
        assertTrue("ファイルが存在すること", outputFile.isFile());
        // TODO uncompress対応までは、手動で確認

        // 出力ファイルパスを指定しない場合
        targetFilePath = DIR_INPUT + "/input.csv";
        outputFilePath = null;
        CompressUtils.compressGz(targetFilePath, outputFilePath);
        outputFile = new File(DIR_INPUT + "/input.csv.gz");
        assertTrue("ファイルが存在すること", outputFile.isFile());
        outputFile.delete();

        // 存在しないディレクトリの場合
        targetFilePath = DIR_INPUT + "/notExist";
        outputFilePath = null;
        try {
            CompressUtils.compressGz(targetFilePath, outputFilePath);
            fail("UtilException をthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("存在チェックのメッセージが返されること", UtilMessageConst.CHECK_NOTEXIST, e.getMessageId());
        }

    }

}

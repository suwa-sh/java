package me.suwash.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import me.suwash.test.DefaultTestWatcher;
import me.suwash.util.constant.UtilMessageConst;
import me.suwash.util.exception.UtilException;
import me.suwash.util.test.UtilTestWatcher;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

@lombok.extern.slf4j.Slf4j
public class CompressUtilsTest {

    private static final String DIR_BASE = "src/test/scripts/util/" + CompressUtilsTest.class.getSimpleName();
    private static final String DIR_INPUT = DIR_BASE + "/input";
//    private static final String DIR_EXPECT = DIR_INPUT;
    private static final String DIR_ACTUAL = DIR_BASE + "/actual";

    @Rule
    public DefaultTestWatcher watcher = new UtilTestWatcher();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        log.debug("■ Init actual dir");
        FileUtils.rmdirs(DIR_ACTUAL);
    }

    @Test
    public void testTarGz() {
        String targetDirPath;
        String outputFilePath;
        boolean isRelative;
        File outputFile;

        // 圧縮（相対パス出力）
        targetDirPath = DIR_INPUT;
        outputFilePath = DIR_ACTUAL + "/testComparessTarGz.tar.gz";
        isRelative = true;
        CompressUtils.tarGz(targetDirPath, outputFilePath, isRelative);
        outputFile = new File(outputFilePath);
        assertTrue("ファイルが存在すること", outputFile.isFile());
        // TODO uncompress対応までは、手動で確認

        // 圧縮（相対パス出力）
        targetDirPath = DIR_INPUT;
        outputFilePath = DIR_ACTUAL + "/testComparessTarGz_absPath.tar.gz";
        isRelative = false;
        CompressUtils.tarGz(targetDirPath, outputFilePath, isRelative);
        outputFile = new File(outputFilePath);
        assertTrue("ファイルが存在すること", outputFile.isFile());

        // 出力ファイルパスを指定しない場合
        targetDirPath = DIR_INPUT;
        outputFilePath = null;
        isRelative = false;
        CompressUtils.tarGz(targetDirPath, outputFilePath, isRelative);
        outputFile = new File(DIR_INPUT + ".tar.gz");
        assertTrue("ファイルが存在すること", outputFile.isFile());
        outputFile.delete();

        // 存在しないディレクトリの場合
        targetDirPath = DIR_INPUT + "/notExist";
        outputFilePath = null;
        isRelative = false;
        try {
            CompressUtils.tarGz(targetDirPath, outputFilePath, isRelative);
            fail("UtilException をthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("存在チェックのメッセージが返されること", UtilMessageConst.CHECK_NOTEXIST, e.getMessageId());
        }

    }

    @Test
    public void testTar() {
        String targetDirPath;
        String outputFilePath;
        boolean isRelative;
        File outputFile;

        // 圧縮（相対パス出力）
        targetDirPath = DIR_INPUT;
        outputFilePath = DIR_ACTUAL + "/testComparessTar.tar";
        isRelative = true;
        CompressUtils.tar(targetDirPath, outputFilePath, isRelative);
        outputFile = new File(outputFilePath);
        assertTrue("ファイルが存在すること", outputFile.isFile());
        // TODO uncompress対応までは、手動で確認

        // 圧縮（相対パス出力）
        targetDirPath = DIR_INPUT;
        outputFilePath = DIR_ACTUAL + "/testComparessTar_absPath.tar";
        isRelative = false;
        CompressUtils.tar(targetDirPath, outputFilePath, isRelative);
        outputFile = new File(outputFilePath);
        assertTrue("ファイルが存在すること", outputFile.isFile());

        // 出力ファイルパスを指定しない場合
        targetDirPath = DIR_INPUT;
        outputFilePath = null;
        isRelative = false;
        CompressUtils.tar(targetDirPath, outputFilePath, isRelative);
        outputFile = new File(DIR_INPUT + ".tar");
        assertTrue("ファイルが存在すること", outputFile.isFile());
        outputFile.delete();

        // 存在しないディレクトリの場合
        targetDirPath = DIR_INPUT + "/notExist";
        outputFilePath = null;
        isRelative = false;
        try {
            CompressUtils.tar(targetDirPath, outputFilePath, isRelative);
            fail("UtilException をthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("存在チェックのメッセージが返されること", UtilMessageConst.CHECK_NOTEXIST, e.getMessageId());
        }

    }

    @Test
    public void testGzip() {
        String targetFilePath;
        String outputFilePath;
        File outputFile;

        // 圧縮
        targetFilePath = DIR_INPUT + "/input.csv";
        outputFilePath = DIR_ACTUAL + "/input.csv.gz";
        CompressUtils.gzip(targetFilePath, outputFilePath);
        outputFile = new File(outputFilePath);
        assertTrue("ファイルが存在すること", outputFile.isFile());
        // TODO uncompress対応までは、手動で確認

        // 出力ファイルパスを指定しない場合
        targetFilePath = DIR_INPUT + "/input.csv";
        outputFilePath = null;
        CompressUtils.gzip(targetFilePath, outputFilePath);
        outputFile = new File(DIR_INPUT + "/input.csv.gz");
        assertTrue("ファイルが存在すること", outputFile.isFile());
        outputFile.delete();

        // 存在しないディレクトリの場合
        targetFilePath = DIR_INPUT + "/notExist";
        outputFilePath = null;
        try {
            CompressUtils.gzip(targetFilePath, outputFilePath);
            fail("UtilException をthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("存在チェックのメッセージが返されること", UtilMessageConst.CHECK_NOTEXIST, e.getMessageId());
        }

    }

}

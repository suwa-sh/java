package me.suwash.util.gen.classification;

import static org.junit.Assert.*;

import java.io.File;

import me.suwash.util.FileUtils;
import me.suwash.util.RuntimeUtils;
import me.suwash.util.TestUtils;
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
public class ClassificationGeneratorTest {

    private static final String DIR_BASE = "src/test/scripts/util/gen/classification/" + ClassificationGeneratorTest.class.getSimpleName();
    private static final String DIR_INPUT = DIR_BASE + "/input";
    private static final String DIR_EXPECT = DIR_BASE + "/expect";
    private static final String DIR_ACTUAL = DIR_BASE + "/actual";

    @Rule
    public TestName name = new TestName();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        log.debug("■■■ " + ClassificationGeneratorTest.class.getName() + " ■■■");
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
        String outputDirPath;
        String ddFileName;
        int errorCount;

        // 正常系
        targetDirPath = DIR_INPUT + "/ok";
        outputDirPath = DIR_ACTUAL + "/ok";
        ddFileName = "dd_source_ok.properties";
        errorCount = ClassificationGenerator.generate(targetDirPath, outputDirPath, ddFileName);
        assertEquals("エラーが発生していないこと", 0, errorCount);
        // 出力内容が期待値と一致すること
        TestUtils.assertDirEquals(DIR_EXPECT + "/ok", DIR_ACTUAL + "/ok");

        // 異常系 パースエラーファイル
        targetDirPath = DIR_INPUT + "/ng";
        outputDirPath = DIR_ACTUAL + "/ng";
        ddFileName = "dd_source_ng.properties";
        errorCount = ClassificationGenerator.generate(targetDirPath, outputDirPath, ddFileName);
        assertEquals("2件のエラーが発生していること", 2, errorCount);

        // 存在しない対象ディレクトリの場合
        targetDirPath = DIR_INPUT + "/notExist";
        outputDirPath = DIR_ACTUAL + "/notExist";
        ddFileName = "dd_source.properties";
        try {
            ClassificationGenerator.generate(targetDirPath, outputDirPath, ddFileName);
            fail("UtilException をthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("存在チェックのメッセージが返されること", UtilMessageConst.CHECK_NOTEXIST, e.getMessageId());
        }

    }

}

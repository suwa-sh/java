package me.suwash.util.gen.classification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import me.suwash.test.DefaultTestWatcher;
import me.suwash.test.TestUtils;
import me.suwash.util.FileUtils;
import me.suwash.util.constant.UtilMessageConst;
import me.suwash.util.exception.UtilException;
import me.suwash.util.test.UtilTestWatcher;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

@lombok.extern.slf4j.Slf4j
public class ClassificationGeneratorTest {

    private static final String DIR_BASE = "src/test/scripts/util/gen/classification/" + ClassificationGeneratorTest.class.getSimpleName();
    private static final String DIR_INPUT = DIR_BASE + "/input";
    private static final String DIR_EXPECT = DIR_BASE + "/expect";
    private static final String DIR_ACTUAL = DIR_BASE + "/actual";

    @Rule
    public DefaultTestWatcher watcher = new UtilTestWatcher();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        log.debug("■ Init actual dir");
        FileUtils.rmdirs(DIR_ACTUAL);
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

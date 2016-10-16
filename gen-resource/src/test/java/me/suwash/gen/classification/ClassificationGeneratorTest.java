package me.suwash.gen.classification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import me.suwash.gen.test.GenTestWatcher;
import me.suwash.test.TestUtils;
import me.suwash.util.FileUtils;
import me.suwash.util.constant.UtilMessageConst;
import me.suwash.util.exception.UtilException;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

@lombok.extern.slf4j.Slf4j
public class ClassificationGeneratorTest {

    private static final String DIR_BASE = "src/test/scripts/gen/classification/" + ClassificationGeneratorTest.class.getSimpleName();
    private static final String DIR_INPUT = DIR_BASE + "/input";
    private static final String DIR_EXPECT = DIR_BASE + "/expect";
    private static final String DIR_ACTUAL = DIR_BASE + "/actual";

    @Rule
    public GenTestWatcher watcher = new GenTestWatcher();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        log.debug("■ Init actual dir");
        FileUtils.rmdirs(DIR_ACTUAL);
    }

    @Test
    public void testCompressTarGz() {
        String targetDirPath;
        String javaOutputDirPath;
        String propsOutputDirPath;
        String ddFileName;
        int errorCount;

        // 正常系
        targetDirPath = DIR_INPUT + "/ok";
        javaOutputDirPath = DIR_ACTUAL + "/ok/java";
        propsOutputDirPath = DIR_ACTUAL + "/ok/resources";
        ddFileName = "dd_source_ok.properties";
        errorCount = ClassificationGenerator.generate(targetDirPath, javaOutputDirPath, propsOutputDirPath, ddFileName);
        assertEquals("エラーが発生していないこと", 0, errorCount);
        // 出力内容が期待値と一致すること
        TestUtils.assertDirEquals(DIR_EXPECT + "/ok", DIR_ACTUAL + "/ok");

        // 異常系 パースエラーファイル
        targetDirPath = DIR_INPUT + "/ng";
        javaOutputDirPath = DIR_ACTUAL + "/ng/java";
        propsOutputDirPath = DIR_ACTUAL + "/ng/resources";
        ddFileName = "dd_source_ng.properties";
        errorCount = ClassificationGenerator.generate(targetDirPath, javaOutputDirPath, propsOutputDirPath, ddFileName);
        assertEquals("2件のエラーが発生していること", 2, errorCount);

        // 存在しない対象ディレクトリの場合
        targetDirPath = DIR_INPUT + "/notExist";
        javaOutputDirPath = DIR_ACTUAL + "/notExist/java";
        propsOutputDirPath = DIR_ACTUAL + "/notExist/resources";
        ddFileName = "dd_source.properties";
        try {
            ClassificationGenerator.generate(targetDirPath, javaOutputDirPath, propsOutputDirPath, ddFileName);
            fail("UtilException をthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("存在チェックのメッセージが返されること", UtilMessageConst.CHECK_NOTEXIST, e.getMessageId());
        }

    }

}

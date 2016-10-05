package me.suwash.test;

import org.junit.Rule;
import org.junit.Test;

//@lombok.extern.slf4j.Slf4j
public class TestUtilsTest {

    private static final String DIR_BASE = "src/test/scripts/test/" + TestUtilsTest.class.getSimpleName();
    private static final String DIR_INPUT = DIR_BASE + "/dir1";
    private static final String DIR_SAME = DIR_BASE + "/dir2";
    private static final String DIR_DIFF1 = DIR_BASE + "/dir3";
    private static final String DIR_DIFF2 = DIR_BASE + "/dir4";

    private static final String CHARSET = "utf8";

    @Rule
    public DefaultTestWatcher watcher = new DefaultTestWatcher();

    @Test
    public void testDirDiff_OK() {
        TestUtils.assertDirEquals(DIR_INPUT, DIR_SAME);
    }

//    @Test
    public void testDirDiff_NG1() {
        TestUtils.assertDirEquals(DIR_INPUT, DIR_DIFF1);
    }

//    @Test
    public void testDirDiff_NG2() {
        TestUtils.assertDirEquals(DIR_INPUT, DIR_DIFF2);
    }

    @Test
    public void testFile_OK() {
        String expectFilePath = DIR_INPUT + "/input.csv";
        String actualFilePath = DIR_BASE + "/same.csv";
        String charset = CHARSET;
        TestUtils.assertFileEquals(expectFilePath, actualFilePath, charset);
    }

//    @Test
    public void testFile_NG() {
        String expectFilePath = DIR_INPUT + "/input.csv";
        String actualFilePath = DIR_BASE + "/diff_empty_row.csv";
        String charset = CHARSET;
        TestUtils.assertFileEquals(expectFilePath, actualFilePath, charset);
    }

}

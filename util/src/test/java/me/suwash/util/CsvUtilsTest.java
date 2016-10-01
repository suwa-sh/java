package me.suwash.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import me.suwash.util.constant.UtilMessageConst;
import me.suwash.util.exception.UtilException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.orangesignal.csv.CsvConfig;
import com.orangesignal.csv.QuotePolicy;

@lombok.extern.slf4j.Slf4j
public class CsvUtilsTest {

    private static final String DIR_BASE = "src/test/scripts/util/" + CsvUtilsTest.class.getSimpleName();
    private static final String DIR_INPUT = DIR_BASE + "/input";
    private static final String DIR_EXPECT = DIR_BASE + "/expect";
    private static final String DIR_ACTUAL = DIR_BASE + "/actual";
    private static final String CHARSET = "utf8";

    @Rule
    public TestName name = new TestName();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        log.debug("■■■ " + CsvUtilsTest.class.getName() + " ■■■");
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
    public void testGetCsvConfig() {
        CsvConfig config = CsvUtils.getCsvConfig();
        assertEquals("区切り文字＝, であること", ',', config.getSeparator());

        assertEquals("括り文字＝\" であること", '"', config.getQuote());
        assertEquals("括り文字のエスケープ文字＝\" であること", '"', config.getEscape());

        assertEquals("改行コード＝システムデフォルト であること", System.getProperty("line.separator"), config.getLineSeparator());
        assertEquals("null文字列＝null であること", null, config.getNullString());

        assertEquals("常に括り文字を付与すること", QuotePolicy.ALL, config.getQuotePolicy());
    }

    @Test
    public void testTsbCsvConfig() {
        CsvConfig config = CsvUtils.getTsvConfig();
        assertEquals("区切り文字＝\t であること", '\t', config.getSeparator());

        assertEquals("括り文字＝\" であること", '"', config.getQuote());
        assertEquals("括り文字のエスケープ文字＝\\ であること", '\\', config.getEscape());

        assertEquals("改行コード＝システムデフォルト であること", System.getProperty("line.separator"), config.getLineSeparator());
        assertEquals("null文字列＝null であること", null, config.getNullString());

        assertEquals("常に括り文字を付与すること", QuotePolicy.ALL, config.getQuotePolicy());
    }

    @Test
    public void testParseWrite() {
        String filePath;
        String charset;
        CsvConfig config;

        String expectFilePath;
        String actualFilePath;

        // parse
        filePath = DIR_INPUT + "/input.csv";
        charset = CHARSET;
        config = CsvUtils.getCsvConfig();
        List<String[]> parsed = CsvUtils.parseFile(filePath, charset, config);

        // write
        List<Map<Integer, String>> dataList;
        String dirPath;
        String fileName;

        dataList = convDataList(parsed);
        dirPath = DIR_ACTUAL;
        fileName = "output.csv";
        charset = CHARSET;
        config = CsvUtils.getCsvConfig();
        CsvUtils.writeFile(dataList, dirPath, fileName, charset, config);

        expectFilePath = DIR_EXPECT + "/" + fileName;
        actualFilePath = DIR_ACTUAL + "/" + fileName;
        TestUtils.assertFileEquals(expectFilePath, actualFilePath, charset);

        // 行列入れ替え
        fileName = "rowToCol.csv";
        CsvUtils.writeFile(CsvUtils.convertRowToCol(parsed), dirPath, fileName, charset, config);

        expectFilePath = DIR_EXPECT + "/" + fileName;
        actualFilePath = DIR_ACTUAL + "/" + fileName;
        TestUtils.assertFileEquals(expectFilePath, actualFilePath, charset);

        // 行列入れ替え.nullの場合
        try {
            CsvUtils.convertRowToCol(null);
            fail("UtilException をthrowすること");
        } catch (UtilException e) {
            log.debug(e.getMessage());
            assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
        }
    }

    private List<Map<Integer, String>> convDataList(List<String[]> parsed) {
        List<Map<Integer, String>> retList = new ArrayList<Map<Integer, String>>();

        for (String[] curLine : parsed) {
            Map<Integer, String> curLineMap = new ConcurrentHashMap<Integer, String>();
            for (int lineIndex = 0; lineIndex < curLine.length; lineIndex++) {
                curLineMap.put(lineIndex, curLine[lineIndex]);
            }
            retList.add(curLineMap);
        }

        return retList;
    }

}

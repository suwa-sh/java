package me.suwash.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class YamlUtilsTest {

   private static final String DIR_BASE = "src/test/scripts/util/" + YamlUtilsTest.class.getSimpleName();
   private static final String CLASSPATH_BASE = "/ut/util/" + YamlUtilsTest.class.getSimpleName();
   private static final String INPUT_CHARSET = "utf8";

   @Rule
   public TestName name = new TestName();

   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
       log.debug("■■■ " + YamlUtilsTest.class.getName() + " ■■■");
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
   public void testParseString() {
       StringBuilder sb = new StringBuilder();
       sb.append("importCache_PMT:").append("\n")
           .append("  csv:").append("\n")
           .append("    MySQL:").append("\n")
           .append("    - テーブル1.csv").append("\n")
           .append("    Redis:").append("\n")
           .append("    - テーブル2.csv").append("\n")
           .append("    DateProp-OK: 2016-06-29T17:37:25.071+9:00").append("\n")
           .append("    DateProp-NG: 2016-06-29 17:37:25").append("\n")
           .append("    DateProp-OK2: 2016-06-29 17:37:25+9:00").append("\n");
       log.debug("----- Input -----");
       log.debug(sb.toString());

       Object object = YamlUtils.parseString(sb.toString(), Object.class);
       log.debug("----- Yaml → Object -----");
       log.debug(object.toString());

       log.debug("----- Object → Yaml -----");
       String yaml = YamlUtils.writeString(object);
       log.debug(yaml);

       log.debug("----- Check -----");
       Object parsed = YamlUtils.parseString(yaml, Object.class);
       assertEquals("ラウンドトリップに変換できていること", 0, CompareUtils.deepCompare(object, parsed));

       assertNull("空文字を変換する場合、nullが返されること", YamlUtils.parseString(StringUtils.EMPTY, Object.class));
       assertNull("nullを変換する場合、nullが返されること", YamlUtils.parseString(null, Object.class));

       try {
           YamlUtils.parseString(sb.toString(), null);
           fail("UtilExceptionがthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
       }

       try {
           sb.append(" IndentError: IndentErrorValue");
           YamlUtils.parseString(sb.toString(), Object.class);
           fail("UtilExceptionがthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertEquals("エラーハンドリングのメッセージが返されること", UtilMessageConst.ERRORHANDLE, e.getMessageId());
       }
   }

   @Test
   public void testParseFile() {
       String dir = DIR_BASE + "/" + name.getMethodName();
       String fileName = "input.yml";

       String inputFilePath = dir + "/" + fileName;
       Object parsed = YamlUtils.parseFile(inputFilePath, INPUT_CHARSET, Object.class);
       log.debug("----- Yaml File → Object -----");
       log.debug(parsed.toString());

       log.debug("----- Object → Yaml -----");
       String yaml = YamlUtils.writeString(parsed);
       log.debug(yaml);

       log.debug("----- Check -----");
       Object reparsed = YamlUtils.parseString(yaml, Object.class);
       assertEquals("ラウンドトリップに変換できていること", 0, CompareUtils.deepCompare(parsed, reparsed));

       log.debug("----- NotExist File -----");
       try {
           fileName = "notExist.yml";
           inputFilePath = dir + "/" + fileName;
           YamlUtils.parseFile(inputFilePath, INPUT_CHARSET, Object.class);
           fail("UtilExceptionがthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertTrue("ファイル名を含むメッセージであること", e.getMessage().contains(fileName));
       }

       log.debug("----- Error File -----");
       try {
           fileName = "error.txt";
           inputFilePath = dir + "/" + fileName;
           YamlUtils.parseFile(inputFilePath, INPUT_CHARSET, Object.class);
           fail("UtilExceptionがthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertTrue("ファイル名を含むメッセージであること", e.getMessage().contains(fileName));
       }

       log.debug("----- Type null -----");
       try {
           YamlUtils.parseFile(inputFilePath, INPUT_CHARSET, null);
           fail("UtilExceptionがthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
       }
   }

   @Test
   public void testParseFileByClasspath() {
       String dir = CLASSPATH_BASE + "/" + name.getMethodName();
       String fileName = "input.yml";

       String inputFilePath = dir + "/" + fileName;
       Object parsed = YamlUtils.parseFileByClasspath(inputFilePath, INPUT_CHARSET, Object.class);
       log.debug("----- Yaml File → Object -----");
       log.debug(parsed.toString());

       log.debug("----- Object → Yaml -----");
       String yaml = YamlUtils.writeString(parsed);
       log.debug(yaml);

       log.debug("----- Check -----");
       Object reparsed = YamlUtils.parseString(yaml, Object.class);
       assertEquals("ラウンドトリップに変換できていること", 0, CompareUtils.deepCompare(parsed, reparsed));

       log.debug("----- NotExist File -----");
       try {
           fileName = "notExist.yml";
           inputFilePath = dir + "/" + fileName;
           YamlUtils.parseFileByClasspath(inputFilePath, INPUT_CHARSET, Object.class);
           fail("UtilExceptionがthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertTrue("ファイル名を含むメッセージであること", e.getMessage().contains(fileName));
       }

       log.debug("----- Error File -----");
       try {
           fileName = "error.txt";
           inputFilePath = dir + "/" + fileName;
           YamlUtils.parseFileByClasspath(inputFilePath, INPUT_CHARSET, Object.class);
           fail("UtilExceptionがthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertTrue("ファイル名を含むメッセージであること", e.getMessage().contains(fileName));
       }

       log.debug("----- Type null -----");
       try {
           YamlUtils.parseFileByClasspath(inputFilePath, INPUT_CHARSET, null);
           fail("UtilExceptionがthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
       }
   }

   @Test
   public void testWriteString() {
       Map<String, Object> innerContent1 = new HashMap<String, Object>();
       innerContent1.put("stringProp", "value1");
       innerContent1.put("numProp", 1);
       innerContent1.put("booleanProp", false);
       innerContent1.put("dateProp", new Date());

       Map<String, Object> innerContent2 = new HashMap<String, Object>();
       innerContent2.put("stringProp", "value2");
       innerContent2.put("numProp", 0.2);
       innerContent2.put("booleanProp", true);
       innerContent2.put("dateProp", new Date());

       List<Map<String, Object>> listContent = new ArrayList<Map<String, Object>>();
       listContent.add(innerContent1);
       listContent.add(innerContent2);

       Map<String, Object> data = new HashMap<String, Object>();
       data.put("listContent", listContent);

       String parsed = YamlUtils.writeString(data);
       log.debug("----- Object → Yaml -----");
       log.debug(parsed);

       Object reversed = YamlUtils.parseString(parsed, Object.class);
       log.debug("----- Yaml → Object -----");
       log.debug(reversed.toString());

       log.debug("----- Check -----");
       assertEquals("ラウンドトリップに変換できていること", 0, CompareUtils.deepCompare(data, reversed));
       assertNull("nullを変換する場合、nullが返されること", YamlUtils.writeString(null));
   }
}

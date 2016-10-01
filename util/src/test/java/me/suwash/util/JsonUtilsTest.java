package me.suwash.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.suwash.util.constant.UtilConst;
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
public class JsonUtilsTest {

   private static final String DIR_BASE = "src/test/scripts/util/" + JsonUtilsTest.class.getSimpleName();
   private static final String CLASSPATH_BASE = "/ut/util/" + JsonUtilsTest.class.getSimpleName();
   private static final String INPUT_CHARSET = "utf8";

   @Rule
   public TestName name = new TestName();

   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
       log.debug("■■■ " + JsonUtilsTest.class.getName() + " ■■■");
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
       sb.append("{").append("\n")
           .append("  \"string\": \"value\"").append(",").append("\n")
           .append("  \"num\": 1.23").append(",").append("\n")
           .append("  \"timestamp\": \"2000-01-01 00:00:00.001\"").append(",").append("\n")
           .append("  \"null\": null").append("\n")
           .append("}").append("\n");
       log.debug("----- Input -----");
       log.debug(sb.toString());

       Object object = JsonUtils.parseString(sb.toString(), Object.class);
       log.debug("----- Json → Object -----");
       log.debug(object.toString());

       log.debug("----- Object → Json -----");
       String json = JsonUtils.writeString(object);
       log.debug(json);

       log.debug("----- Check -----");
       Object parsed = JsonUtils.parseString(json, Object.class);
       assertEquals("ラウンドトリップに変換できていること", 0, CompareUtils.deepCompare(object, parsed));

       assertNull("空文字を変換する場合、nullが返されること", JsonUtils.parseString(StringUtils.EMPTY, Object.class));
       assertNull("nullを変換する場合、nullが返されること", JsonUtils.parseString(null, Object.class));

       try {
           JsonUtils.parseString(sb.toString(), null);
           fail("UtilExceptionがthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
       }

       try {
           sb.delete(11, 20);
           JsonUtils.parseString(sb.toString(), Object.class);
           fail("UtilExceptionがthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertEquals("エラーハンドリングのメッセージが返されること", UtilMessageConst.ERRORHANDLE, e.getMessageId());
       }
   }

   @Test
   public void testParseFile() {
       String dir = DIR_BASE + "/" + name.getMethodName();
       String fileName = "input.json";

       String inputFilePath = dir + "/" + fileName;
       Object parsed = JsonUtils.parseFile(inputFilePath, INPUT_CHARSET, Object.class);
       log.debug("----- Json File → Object -----");
       log.debug(parsed.toString());

       log.debug("----- Object → Json -----");
       String json = JsonUtils.writeString(parsed);
       log.debug(json);

       log.debug("----- Check -----");
       Object reparsed = JsonUtils.parseString(json, Object.class);
       assertEquals("ラウンドトリップに変換できていること", 0, CompareUtils.deepCompare(parsed, reparsed));

       log.debug("----- NotExist File -----");
       try {
           fileName = "notExist.json";
           inputFilePath = dir + "/" + fileName;
           JsonUtils.parseFile(inputFilePath, INPUT_CHARSET, Object.class);
           fail("UtilExceptionがthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertTrue("ファイル名を含むメッセージであること", e.getMessage().contains(fileName));
       }

       log.debug("----- Error File -----");
       try {
           fileName = "error.txt";
           inputFilePath = dir + "/" + fileName;
           JsonUtils.parseFile(inputFilePath, INPUT_CHARSET, Object.class);
           fail("UtilExceptionがthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertTrue("ファイル名を含むメッセージであること", e.getMessage().contains(fileName));
       }

       log.debug("----- Type null -----");
       try {
           JsonUtils.parseFile(inputFilePath, INPUT_CHARSET, null);
           fail("UtilExceptionがthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
       }
   }

   @Test
   public void testParseFileByClasspath() {
       String dir = CLASSPATH_BASE + "/" + name.getMethodName();
       String fileName = "input.json";

       String inputFilePath = dir + "/" + fileName;
       Object parsed = JsonUtils.parseFileByClasspath(inputFilePath, INPUT_CHARSET, Object.class);
       log.debug("----- Json File → Object -----");
       log.debug(parsed.toString());

       log.debug("----- Object → Json -----");
       String json = JsonUtils.writeString(parsed);
       log.debug(json);

       log.debug("----- Check -----");
       Object reparsed = JsonUtils.parseString(json, Object.class);
       assertEquals("ラウンドトリップに変換できていること", 0, CompareUtils.deepCompare(parsed, reparsed));

       log.debug("----- NotExist File -----");
       try {
           fileName = "notExist.json";
           inputFilePath = dir + "/" + fileName;
           JsonUtils.parseFileByClasspath(inputFilePath, INPUT_CHARSET, Object.class);
           fail("UtilExceptionがthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertTrue("ファイル名を含むメッセージであること", e.getMessage().contains(fileName));
       }

       log.debug("----- Error File -----");
       try {
           fileName = "error.txt";
           inputFilePath = dir + "/" + fileName;
           JsonUtils.parseFileByClasspath(inputFilePath, INPUT_CHARSET, Object.class);
           fail("UtilExceptionがthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertTrue("ファイル名を含むメッセージであること", e.getMessage().contains(fileName));
       }

       log.debug("----- Type null -----");
       try {
           JsonUtils.parseFileByClasspath(inputFilePath, INPUT_CHARSET, null);
           fail("UtilExceptionがthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
       }
   }

   @Test
   public void testWriteString() {
       // TODO Yamlだと、フォーマット指定なしでもparseできている様子。合わせた方が良いか？
       DateFormat dateFormat = new SimpleDateFormat(UtilConst.DATE_FORMAT_ISO8601_MILLISECOND);
       Map<String, Object> innerContent1 = new HashMap<String, Object>();
       innerContent1.put("stringProp", "value1");
       innerContent1.put("numProp", 1);
       innerContent1.put("booleanProp", false);
       innerContent1.put("dateProp", dateFormat.format(new Date()));

       Map<String, Object> innerContent2 = new HashMap<String, Object>();
       innerContent2.put("stringProp", "value2");
       innerContent2.put("numProp", 0.2);
       innerContent2.put("booleanProp", true);
       innerContent2.put("dateProp", dateFormat.format(new Date()));

       List<Map<String, Object>> listContent = new ArrayList<Map<String, Object>>();
       listContent.add(innerContent1);
       listContent.add(innerContent2);

       Map<String, Object> data = new HashMap<String, Object>();
       data.put("listContent", listContent);

       String parsed = JsonUtils.writeString(data);
       log.debug("----- Object → Json -----");
       log.debug(parsed);

       Object reversed = JsonUtils.parseString(parsed, Object.class);
       log.debug("----- Json → Object -----");
       log.debug(reversed.toString());

       log.debug("----- Check -----");
       log.debug("data    : " + data);
//       log.debug("parsed  : " + parsed);
       log.debug("reversed: " + reversed);
       assertTrue("ラウンドトリップに変換できていること", CompareUtils.deepCompare(data, reversed) == 0);
       assertNull("nullを変換する場合、nullが返されること", JsonUtils.writeString(null));

       log.debug("----- Check - pretty -----");
       log.debug(JsonUtils.writePrettyString(data));
       assertNull("nullを変換する場合、nullが返されること", JsonUtils.writePrettyString(null));
   }
}

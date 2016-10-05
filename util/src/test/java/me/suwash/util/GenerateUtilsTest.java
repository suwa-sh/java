package me.suwash.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.suwash.test.DefaultTestWatcher;
import me.suwash.util.constant.UtilMessageConst;
import me.suwash.util.exception.UtilException;
import me.suwash.util.test.UtilTestWatcher;

import org.apache.commons.lang.StringUtils;
import org.junit.Rule;
import org.junit.Test;

@lombok.extern.slf4j.Slf4j
public class GenerateUtilsTest {

    private static final String DIR_BASE = "src/test/scripts/util/" + GenerateUtilsTest.class.getSimpleName();
    private static final String CLASSPATH_BASE = "/ut/util/" + GenerateUtilsTest.class.getSimpleName();
    private static final String OUTPUT_CHARSET = "utf8";
    private static final String OUTPUT_LINESP = "\n";

    @Rule
    public DefaultTestWatcher watcher = new UtilTestWatcher();

   @Test
   public void testTemplate2String() {
       String template = "mapContent.key1: ${mapContent.key1}, mapContent.key2: ${mapContent.key2}";
       Map<String, String> mapContent = new HashMap<String, String>();
       mapContent.put("key1", "this is key1!");
       mapContent.put("key2", "${KEY2_VALUE}");
       Map<String, Object> contextMap = new HashMap<String, Object>();
       contextMap.put("mapContent", mapContent);
       String generated = GenerateUtils.template2String(template, contextMap);
       log.debug("----- mapContent -----");
       log.debug(generated);

       StringBuilder sb = new StringBuilder();
       sb.append("#foreach( ${elem} in ${listContent} )").append("\n")
           .append("${elem}").append("\n")
           .append("#end").append("\n");
       template = sb.toString();
       List<String> listContent = new ArrayList<String>();
       listContent.add("elem1");
       listContent.add("elem2");
       contextMap.clear();
       contextMap.put("listContent", listContent);
       generated = GenerateUtils.template2String(template, contextMap);
       log.debug("----- listContent -----");
       log.debug(generated);

       sb = new StringBuilder();
       sb.append("#ERRORforeach( ${elem} in ${listContent} )").append("\n")
           .append("${elem}").append("\n")
           .append("#end").append("\n");
       template = sb.toString();
       try {
           generated = GenerateUtils.template2String(template, contextMap);
           fail("UtilException がthrowされること");

       } catch (UtilException e) {
           assertTrue("エラーメッセージに template が含まれていること", e.getMessage().contains(template));
       }
   }

   @Test
   public void testVmPath2File() {
       String templateDirPath = DIR_BASE + "/testVmPath2File";

       List<String> templateDirPathList = null;
       String templateFileName = null;
       Map<String, Object> contextMap = null;
       String outputFilePath = null;
       String outputCharset = null;
       String outputLineSp = null;

       try {
           GenerateUtils.vmPath2File(templateDirPathList, templateFileName, contextMap, outputFilePath, outputCharset, outputLineSp);
           fail("UtilException をthrowすること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
       }

       outputFilePath = templateDirPath + "/actual/generated.txt";
       try {
           GenerateUtils.vmPath2File(templateDirPathList, templateFileName, contextMap, outputFilePath, outputCharset, outputLineSp);
           fail("UtilException がthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
       }

       outputCharset = OUTPUT_CHARSET;
       try {
           GenerateUtils.vmPath2File(templateDirPathList, templateFileName, contextMap, outputFilePath, outputCharset, outputLineSp);
           fail("UtilException がthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
       }

       outputLineSp = OUTPUT_LINESP;
       try {
           GenerateUtils.vmPath2File(templateDirPathList, templateFileName, contextMap, outputFilePath, outputCharset, outputLineSp);
           fail("UtilException をthrowすること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
       }

       templateDirPathList = new ArrayList<String>();
       templateDirPathList.add(templateDirPath);
       try {
           GenerateUtils.vmPath2File(templateDirPathList, templateFileName, contextMap, outputFilePath, outputCharset, outputLineSp);
           fail("UtilException をthrowすること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
       }

       templateFileName = "notExist.vm";
       try {
           GenerateUtils.vmPath2File(templateDirPathList, templateFileName, contextMap, outputFilePath, outputCharset, outputLineSp);
           fail("UtilException がthrowされること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertEquals("存在チェックのメッセージが返されること", UtilMessageConst.CHECK_NOTEXIST, e.getMessageId());
           assertTrue("エラーメッセージに templateFileName が含まれていること", e.getMessage().contains(templateFileName));
       }

       templateFileName = "template.vm";
       GenerateUtils.vmPath2File(templateDirPathList, templateFileName, contextMap, outputFilePath, outputCharset, outputLineSp);
       // TODO コンテキストマップなしでの出力チェック。まずは正常終了でもOK。

       Map<String, String> content = new HashMap<String, String>();
       content.put("key1", "key1-value");
       content.put("key2", "key2-value");
       contextMap = new HashMap<String, Object>();
       contextMap.put("content", content);
       GenerateUtils.vmPath2File(templateDirPathList, templateFileName, contextMap, outputFilePath, outputCharset, outputLineSp);
   }

   @Test
   public void testVmClasspath2File() {
       String templateDirClasspath = CLASSPATH_BASE + "/testVmClasspath2File";
       String templateFileName = StringUtils.EMPTY;

       List<String> templateDirPathList = new ArrayList<String>();
       templateDirPathList.add(templateDirClasspath);

       Map<String, String> content = new HashMap<String, String>();
       content.put("key1", "key1-value");
       content.put("key2", "key2-value");
       Map<String, Object> contextMap = new HashMap<String, Object>();
       contextMap.put("content", content);

       String outputCharset = OUTPUT_CHARSET;
       String outputLineSp = OUTPUT_LINESP;
       String outputFilePath = DIR_BASE + "/testVmClasspath2File/actual/generated.txt";

       try {
           templateFileName = "notExist.vm";
           GenerateUtils.vmClasspath2File(templateDirPathList, templateFileName, contextMap, outputFilePath, outputCharset, outputLineSp);
           fail("UtilException がthrowされること");
       } catch (UtilException e) {
           assertTrue("エラーメッセージに templateFileName が含まれていること", e.getMessage().contains(templateFileName));
       }

       templateFileName = "template.vm";
       GenerateUtils.vmClasspath2File(templateDirPathList, templateFileName, contextMap, outputFilePath, outputCharset, outputLineSp);
   }

   @Test
   public void testVmClasspath2File_withCommonTemplate() {
       String commonTemplateDirClasspath = CLASSPATH_BASE + "/defs/_common/templates";
       String templateDirClasspath = CLASSPATH_BASE + "/testVmClasspath2File";
       String templateFileName = StringUtils.EMPTY;

       List<String> templateDirPathList = new ArrayList<String>();
       templateDirPathList.add(commonTemplateDirClasspath);
       templateDirPathList.add(templateDirClasspath);

       List<String> content = new ArrayList<String>();
       content.add("this is LINE-1.");
       content.add("this is LINE-2.");
       content.add("\n this is LINE-3. ※先頭に改行コード付き");
       Map<String, Object> contextMap = new HashMap<String, Object>();
       contextMap.put("content", content);

       String outputCharset = OUTPUT_CHARSET;
       String outputLineSp = OUTPUT_LINESP;
       String outputFilePath = DIR_BASE + "/testVmClasspath2File_withCommonTemplate/actual/notExist.txt";

       try {
           templateFileName = "notExist.vm";
           GenerateUtils.vmClasspath2File(templateDirPathList, templateFileName, contextMap, outputFilePath, outputCharset, outputLineSp);
           fail("UtilException がthrowされること");
       } catch (UtilException e) {
           assertTrue("エラーメッセージに templateFileName が含まれていること", e.getMessage().contains(templateFileName));
       }

       templateFileName = "list2line.vm";
       outputFilePath = DIR_BASE + "/testVmClasspath2File_withCommonTemplate/actual/list2line.txt";
       GenerateUtils.vmClasspath2File(templateDirPathList, templateFileName, contextMap, outputFilePath, outputCharset, outputLineSp);

       templateFileName = "empty.vm";
       outputFilePath = DIR_BASE + "/testVmClasspath2File_withCommonTemplate/actual/empty.txt";
       GenerateUtils.vmClasspath2File(templateDirPathList, templateFileName, contextMap, outputFilePath, outputCharset, outputLineSp);
   }

   @Test
   public void testVmPath2String() {
       String templateDirPath = DIR_BASE + "/testVmPath2File";
       String templateFileName = StringUtils.EMPTY;

       List<String> templateDirPathList = new ArrayList<String>();
       templateDirPathList.add(templateDirPath);

       Map<String, String> content = new HashMap<String, String>();
       content.put("key1", "key1-value");
       content.put("key2", "key2-value");
       Map<String, Object> contextMap = new HashMap<String, Object>();
       contextMap.put("content", content);

       try {
           templateFileName = "notExist.vm";
           GenerateUtils.vmPath2String(templateDirPathList, templateFileName, contextMap);
           fail("UtilException がthrowされること");
       } catch (UtilException e) {
           assertTrue("エラーメッセージに templateFileName が含まれていること", e.getMessage().contains(templateFileName));
       }

       templateFileName = "template.vm";
       String generated = GenerateUtils.vmPath2String(templateDirPathList, templateFileName, contextMap);
       log.debug(generated);
   }

   @Test
   public void testVmClasspath2String() {
       String templateDirClasspath = CLASSPATH_BASE + "/testVmClasspath2File";
       String templateFileName = StringUtils.EMPTY;

       List<String> templateDirPathList = new ArrayList<String>();
       templateDirPathList.add(templateDirClasspath);

       Map<String, String> content = new HashMap<String, String>();
       content.put("key1", "key1-value");
       content.put("key2", "key2-value");
       Map<String, Object> contextMap = new HashMap<String, Object>();
       contextMap.put("content", content);

       try {
           templateFileName = "notExist.vm";
           GenerateUtils.vmClasspath2String(templateDirPathList, templateFileName, contextMap);
           fail("UtilException がthrowされること");
       } catch (UtilException e) {
           assertTrue("エラーメッセージに templateFileName が含まれていること", e.getMessage().contains(templateFileName));
       }

       templateFileName = "template.vm";
       String generated = GenerateUtils.vmClasspath2String(templateDirPathList, templateFileName, contextMap);
       log.debug(generated);
   }

}

package me.suwash.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class ObjectTreeUtilsTest {

    private static final String VALUE_STRING = "this is String-Value!";
    private static final double VALUE_NUM = 3.3;
    private static final boolean VALUE_BOOLEAN = true;
    private static final Date VALUE_DATE = new Date();

    @Rule
   public TestName name = new TestName();

   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
       log.debug("■■■ " + ObjectTreeUtilsTest.class.getName() + " ■■■");
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
   public void test() {
       //------------------------------
       // 準備
       //------------------------------
       final Map<String, String> depth5Map = new HashMap<String, String>();
       depth5Map.put("key5-string", VALUE_STRING);

       final List<Object> depth4List = new ArrayList<Object>();
       depth4List.add("elem1");
       depth4List.add("elem2");
       depth4List.add(depth5Map);

       Map<String, Object> depth3 = new HashMap<String, Object>();
       depth3.put("key3-string", VALUE_STRING);
       depth3.put("key3-num", VALUE_NUM);
       depth3.put("key3-boolean", VALUE_BOOLEAN);
       depth3.put("key3-date", VALUE_DATE);
       depth3.put("key3-list", depth4List);

       Map<String, Object> depth2Map = new HashMap<String, Object>();
       depth2Map.put("key2-map", depth3);

       List<String> depth2List = new ArrayList<String>();
       depth2List.add("elem1");
       depth2List.add("elem2");
       depth2List.add("elem3");

       SampleCustomBean depth2Bean = new SampleCustomBean("bean name");

       Map<String, Object> depth1 = new HashMap<String, Object>();
       depth1.put("key1-map", depth2Map);
       depth1.put("key1-list", depth2List);
       depth1.put("key1-bean", depth2Bean);

       //------------------------------
       // getSubTree
       //------------------------------
       assertEquals("", depth2Bean, ObjectTreeUtils.getSubTree(depth1, "key1-bean"));
       assertEquals("", depth2List, ObjectTreeUtils.getSubTree(depth1, ".key1-list"));
       assertEquals("", "elem2", ObjectTreeUtils.getSubTree(depth1, "key1-list.1"));
       assertEquals("", "elem3", ObjectTreeUtils.getSubTree(depth1, "key1-list.[2]"));
       assertEquals("", depth2Map, ObjectTreeUtils.getSubTree(depth1, "key1-map"));
       assertEquals("", depth3, ObjectTreeUtils.getSubTree(depth1, "key1-map.key2-map"));
       assertEquals("", VALUE_STRING, ObjectTreeUtils.getSubTree(depth1, "key1-map.key2-map.key3-string"));
       assertEquals("", VALUE_NUM, ObjectTreeUtils.getSubTree(depth1, "key1-map.key2-map.key3-num"));
       assertEquals("", VALUE_BOOLEAN, ObjectTreeUtils.getSubTree(depth1, "key1-map.key2-map.key3-boolean"));
       assertEquals("", VALUE_DATE, ObjectTreeUtils.getSubTree(depth1, "key1-map.key2-map.key3-date"));
       assertEquals("", VALUE_STRING, ObjectTreeUtils.getSubTree(depth1, "key1-map.key2-map.key3-list.[2].key5-string"));

       assertEquals("存在しないキーの場合", null, ObjectTreeUtils.getSubTree(depth1, "key1-map.key2-map.NOT-EXIST"));
       assertEquals("存在しない要素の場合", null,ObjectTreeUtils.getSubTree(depth1, "key1-list.3"));
       // TODO Bean対応
       assertEquals("未対応の型の場合", null, ObjectTreeUtils.getSubTree(depth1, "key1-bean.name"));

       try {
           ObjectTreeUtils.getSubTree(null, "key1-bean");
           fail("UtilExceptionをthrowすること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
       }

       try {
           ObjectTreeUtils.getSubTree(depth1, null);
           fail("UtilExceptionをthrowすること");
       } catch (UtilException e) {
           log.debug(e.getMessage());
           assertEquals("nullチェックのメッセージが返されること", UtilMessageConst.CHECK_NOTNULL, e.getMessageId());
       }

       //------------------------------
       // createTree
       //------------------------------
       String path;

       // null指定の場合
       path = null;
       assertNull("nullが返されること", ObjectTreeUtils.createTree(path));

       // 正常系
       path = "key1-map.key2-map";
       Map<String, Object> generated = ObjectTreeUtils.createTree(path);
       log.debug(generated.toString());


       //------------------------------
       // setSubTree
       //------------------------------
       // TODO Bean対応の呼び出し。
       // 正常系
       path = "key1-map.key2-map.key3-list";
       ObjectTreeUtils.setSubTree(generated, path, depth4List);
       log.debug(generated.toString());


       //------------------------------
       // merge
       //------------------------------
       // deepCompare向けに、Beanを除去
       depth1.remove("key1-bean");
       String removedBaseString = depth1.toString();

       // ルート要素からmerge
       ObjectTreeUtils.mergeTree(generated, depth1);
       log.debug(generated.toString());
       assertTrue("同一パスが、全て上書きされていること", CompareUtils.deepCompare(depth1, generated) == 0);

       // 差分を追加
       Map<String, Object> addMap = new HashMap<String, Object>();
       ObjectTreeUtils.setSubTree(generated, "key1-add", addMap);
       ObjectTreeUtils.mergeTree(generated, depth1);
       assertTrue("マージ元に存在しないパスは、変更されていないこと", CompareUtils.deepCompare(depth1, generated) < 0);
       assertEquals("マージ元に存在しないパスは、変更されていないこと", addMap, generated.get("key1-add"));

       // マージ元がnullの場合
       String addedGeneratedString = generated.toString();
       ObjectTreeUtils.mergeTree(generated, null);
       assertEquals("null指定の場合、何も変更されないこと", addedGeneratedString, generated.toString());
       assertEquals("null指定の場合、何も変更されないこと", removedBaseString, depth1.toString());

       // マージ先がnullの場合
       ObjectTreeUtils.mergeTree(null, depth1);
       assertEquals("null指定の場合、何も変更されないこと", addedGeneratedString, generated.toString());
       assertEquals("null指定の場合、何も変更されないこと", removedBaseString, depth1.toString());
   }

   @lombok.Data
   @lombok.AllArgsConstructor
   class SampleCustomBean {
       private String name;
   }
}

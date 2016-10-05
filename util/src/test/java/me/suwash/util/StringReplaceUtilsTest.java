package me.suwash.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.suwash.test.DefaultTestWatcher;
import me.suwash.util.test.UtilTestWatcher;

import org.junit.Rule;
import org.junit.Test;

@lombok.extern.slf4j.Slf4j
public class StringReplaceUtilsTest {

    @Rule
    public DefaultTestWatcher watcher = new UtilTestWatcher();

   @Test
   public void testReplace() {
       final String VALUE_STRING = "this is String-Value!";
       final double VALUE_NUM = 3.3;
       final boolean VALUE_BOOLEAN = true;
       final Date VALUE_DATE = new Date();

       Map<String, Object> depth3 = new HashMap<String, Object>();
       depth3.put("key3-string", VALUE_STRING);
       depth3.put("key3-boolean", VALUE_BOOLEAN);
       depth3.put("key3-num", VALUE_NUM);
       depth3.put("key3-date", VALUE_DATE);

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

       StringBuilder sb = new StringBuilder();
       sb
           .append("bean:").append("\n")
           .append("  ${key1-bean}").append("\n")
           .append("list:").append("\n")
           .append("  ${key1-list.[0]},${key1-list.[1]},${key1-list.[2]}").append("\n")
           .append("map:").append("\n")
           .append("  key3-string: '${key1-map.key2-map.key3-string}'").append("\n")
           .append("  key3-boolean: ${key1-map.key2-map.key3-boolean}").append("\n")
           .append("  key3-num: ${key1-map.key2-map.key3-num}").append("\n")
           .append("  key3-date: ${key1-map.key2-map.key3-date}").append("\n")
           ;
       String replaced = StringReplaceUtils.replace(sb.toString(), depth1);
       log.debug(replaced);

   }

   class SampleCustomBean {
       private String name;
       public SampleCustomBean(String name) {
           this.name = name;
       }
       public String getName() {
           return name;
       }
   }
}

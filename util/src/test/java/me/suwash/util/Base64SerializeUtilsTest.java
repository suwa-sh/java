package me.suwash.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

@lombok.extern.slf4j.Slf4j
public class Base64SerializeUtilsTest {

    @Rule
    public TestName name = new TestName();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        log.debug("■■■ " + Base64SerializeUtilsTest.class.getName() + " ■■■");
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
        Map<String, String> map = new HashMap<String, String>();
        map.put("map.string", "STRING VALUE");

        List<String> list = new ArrayList<String>();
        list.add("STRING VALUE");

        Map<String, Object> target = new HashMap<String, Object>();
        target.put("map", map);
        target.put("list", list);
        target.put("string", "STRING VALUE");

        // serialize
        String serialized = Base64SerializeUtils.serialize(target);
        log.debug("serialized: " + serialized);
        // deserialize
        Object deserialized = Base64SerializeUtils.deserialize(serialized);
        assertTrue("復元されていること", CompareUtils.deepCompare(target, deserialized) == 0);

        // nullの場合
        serialized = Base64SerializeUtils.serialize(null);
        log.debug("serialized: " + serialized);
        deserialized = Base64SerializeUtils.deserialize(serialized);
        assertNull("復元されていること", deserialized);
    }

}

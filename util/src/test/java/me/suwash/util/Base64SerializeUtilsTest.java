package me.suwash.util;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.suwash.test.DefaultTestWatcher;
import me.suwash.util.test.UtilTestWatcher;

import org.junit.Rule;
import org.junit.Test;

@lombok.extern.slf4j.Slf4j
public class Base64SerializeUtilsTest {

    @Rule
    public DefaultTestWatcher watcher = new UtilTestWatcher();

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

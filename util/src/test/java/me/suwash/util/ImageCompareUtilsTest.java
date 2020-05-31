package me.suwash.util;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.suwash.test.DefaultTestWatcher;
import me.suwash.util.CompareUtils.CompareCriteria;
import me.suwash.util.CompareUtils.CompareStatus;
import me.suwash.util.test.UtilTestWatcher;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;

@lombok.extern.slf4j.Slf4j
public class ImageCompareUtilsTest {

    @Rule
    public DefaultTestWatcher watcher = new UtilTestWatcher();

    @Test
    public void test() {
      List<String> allowdExtList = ImageCompareUtils.getAllowedExtList();
      assertThat(allowdExtList, hasItem("png"));
      assertThat(allowdExtList, hasItem("gif"));
      assertThat(allowdExtList, hasItem("jpg"));

      assertThat(ImageCompareUtils.isAllowedExt("png"), is(true));
      assertThat(ImageCompareUtils.isAllowedExt("gif"), is(true));
      assertThat(ImageCompareUtils.isAllowedExt("jpg"), is(true));
      assertThat(ImageCompareUtils.isAllowedExt("unknown"), is(false));
//        assertEquals("value値が大きい場合、1を返すこと", 1, CompareUtils.deepCompare(map4, map1));
    }

}
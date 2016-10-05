package me.suwash.util;

import static org.junit.Assert.assertEquals;
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
public class CompareUtilsTest {

    @Rule
    public DefaultTestWatcher watcher = new UtilTestWatcher();

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testDeepCompare() {
        Map<String, Object> map1 = getMap();

        Map<String, Object> map2 = getMap();
        ((List) map2.get("nestList")).add("nestList3");

        Map<String, Object> map3 = getMap();
        ((Map) map3.get("nestMap")).put("nestMap3", new Integer(3));

        Map<String, Object> map4 = getMap();
        map4.put("value2", 2.1);

        assertEquals("同一インスタンスの場合、0を返すこと", 0, CompareUtils.deepCompare(map1, map1));
        assertEquals("同一値の場合、0を返すこと", 0, CompareUtils.deepCompare(map1, getMap()));

        assertEquals("左がnullの場合、-1を返すこと", -1, CompareUtils.deepCompare(null, map2));
        assertEquals("左右がnullの場合、0を返すこと", 0, CompareUtils.deepCompare(null, null));
        assertEquals("右がnullの場合、1を返すこと", 1, CompareUtils.deepCompare(map1, null));

        assertEquals("Listの要素が少ない場合、-1を返すこと", -1, CompareUtils.deepCompare(map1, map2));
        assertEquals("Listの要素が多い場合、1を返すこと", 1, CompareUtils.deepCompare(map2, map1));

        assertEquals("Mapの要素が少ない場合、-1を返すこと", -1, CompareUtils.deepCompare(map1, map3));
        assertEquals("Mapの要素が多い場合、1を返すこと", 1, CompareUtils.deepCompare(map3, map1));

        assertEquals("value値が小さい場合、-1を返すこと", -1, CompareUtils.deepCompare(map1, map4));
        assertEquals("value値が大きい場合、1を返すこと", 1, CompareUtils.deepCompare(map4, map1));

    }
    private Map<String, Object> getMap() {
        Map<String, Object> nestMap = new LinkedHashMap<String, Object>();
        nestMap.put("nestMap1", "1");
        nestMap.put("nestMap2", 2);

        List<String> nestList = new ArrayList<String>();
        nestList.add("nestList1");
        nestList.add("nestList2");

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("nestMap", nestMap);
        map.put("nestList", nestList);
        map.put("value1", "1");
        map.put("value2", 2);
        return map;
    }

    @Test
    public void testCompareInCriteria() {
        //--------------------------------------------------------------------------------
        // 比較条件：null
        //--------------------------------------------------------------------------------
        String str1 = "string";
        String str2 = "STRING";
        assertEquals("null OK", CompareStatus.OK, CompareUtils.compareInCriteria(null, str1, str1));
        assertEquals("null NG", CompareStatus.NG, CompareUtils.compareInCriteria(null, str1, str2));
        // nullチェック
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(null, null, str1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(null, str1, null));
        assertEquals("nullチェック", CompareStatus.OK, CompareUtils.compareInCriteria(null, null, null));

        //--------------------------------------------------------------------------------
        // 除外
        //--------------------------------------------------------------------------------
        assertEquals("除外", CompareStatus.Ignore, CompareUtils.compareInCriteria(CompareCriteria.Ignore, str1, StringUtils.EMPTY));
        // nullチェック
        assertEquals("nullチェック", CompareStatus.Ignore, CompareUtils.compareInCriteria(CompareCriteria.Ignore, null, StringUtils.EMPTY));
        assertEquals("nullチェック", CompareStatus.Ignore, CompareUtils.compareInCriteria(CompareCriteria.Ignore, StringUtils.EMPTY, null));
        assertEquals("nullチェック", CompareStatus.Ignore, CompareUtils.compareInCriteria(CompareCriteria.Ignore, null, null));

        //--------------------------------------------------------------------------------
        // 一致
        //--------------------------------------------------------------------------------
        assertEquals("＝＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Equal, str1, str1));
        assertEquals("＝＝ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Equal, str1, str2));
        // nullチェック
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Equal, null, str1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Equal, str1, null));
        assertEquals("nullチェック", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Equal, null, null));

        //--------------------------------------------------------------------------------
        // 不一致
        //--------------------------------------------------------------------------------
        assertEquals("！＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.NotEqual, str1, str2));
        assertEquals("！＝ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.NotEqual, str1, str1));
        // nullチェック
        assertEquals("nullチェック", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.NotEqual, null, str1));
        assertEquals("nullチェック", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.NotEqual, str1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.NotEqual, null, null));

        //--------------------------------------------------------------------------------
        // 数値
        //--------------------------------------------------------------------------------
        String num1 = "9223372036854775806.999999999999999999999999999999";
        String num2 = "9223372036854775807";
        String num3 = "9223372036854775808";
        assertEquals("数値＜ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Number_GraterThan_Left, num1, num2));
        assertEquals("数値＜ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Number_GraterThan_Left, num1, num2));
        assertEquals("数値＜ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Number_GraterThan_Left, num1, num2));
        assertEquals("数値＜ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Number_GraterThan_Left, num2, num2));
        assertEquals("数値＜ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Number_GraterThan_Left, num2, num1));
        assertEquals("数値＞ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Number_LessThan_Left, num2, num1));
        assertEquals("数値＞ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Number_LessThan_Left, num2, num2));
        assertEquals("数値＞ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Number_LessThan_Left, num1, num2));
        assertEquals("数値＜＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Number_GraterEqualThan_Left, num2, num3));
        assertEquals("数値＜＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Number_GraterEqualThan_Left, num2, num2));
        assertEquals("数値＜＝ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Number_GraterEqualThan_Left, num2, num1));
        assertEquals("数値＞＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Number_LessEqualThan_Left, num2, num1));
        assertEquals("数値＞＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Number_LessEqualThan_Left, num2, num2));
        assertEquals("数値＞＝ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Number_LessEqualThan_Left, num2, num3));
        // nullチェック
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Number_GraterThan_Left, null, num1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Number_GraterThan_Left, num1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Number_GraterThan_Left, null, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Number_LessThan_Left, null, num1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Number_LessThan_Left, num1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Number_LessThan_Left, null, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Number_GraterEqualThan_Left, null, num1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Number_GraterEqualThan_Left, num1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Number_GraterEqualThan_Left, null, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Number_LessEqualThan_Left, null, num1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Number_LessEqualThan_Left, num1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Number_LessEqualThan_Left, null, null));
        // parseエラー
        try { CompareUtils.compareInCriteria(CompareCriteria.Number_GraterThan_Left, str1, num1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Number_GraterThan_Left, num1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Number_GraterThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Number_LessThan_Left, str1, num1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Number_LessThan_Left, num1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Number_LessThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Number_GraterEqualThan_Left, str1, num1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Number_GraterEqualThan_Left, num1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Number_GraterEqualThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Number_LessEqualThan_Left, str1, num1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Number_LessEqualThan_Left, num1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Number_LessEqualThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }

        //--------------------------------------------------------------------------------
        // 年
        //--------------------------------------------------------------------------------
        String year1 = "2014";
        String year2 = "2015";
        String year3 = "2016";
        assertEquals("年＜ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Year_GraterThan_Left, year2, year3));
        assertEquals("年＜ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Year_GraterThan_Left, year2, year2));
        assertEquals("年＜ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Year_GraterThan_Left, year2, year1));
        assertEquals("年＞ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Year_LessThan_Left, year2, year1));
        assertEquals("年＞ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Year_LessThan_Left, year2, year2));
        assertEquals("年＞ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Year_LessThan_Left, year2, year3));
        assertEquals("年＜＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Year_GraterEqualThan_Left, year1, year2));
        assertEquals("年＜＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Year_GraterEqualThan_Left, year2, year2));
        assertEquals("年＜＝ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Year_GraterEqualThan_Left, year3, year2));
        assertEquals("年＞＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Year_LessEqualThan_Left, year3, year2));
        assertEquals("年＜＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Year_LessEqualThan_Left, year2, year2));
        assertEquals("年＞＝ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Year_LessEqualThan_Left, year1, year2));
        // nullチェック
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Year_GraterThan_Left, null, year1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Year_GraterThan_Left, year1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Year_GraterThan_Left, null, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Year_LessThan_Left, null, year1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Year_LessThan_Left, year1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Year_LessThan_Left, null, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Year_GraterEqualThan_Left, null, year1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Year_GraterEqualThan_Left, year1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Year_GraterEqualThan_Left, null, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Year_LessEqualThan_Left, null, year1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Year_LessEqualThan_Left, year1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Year_LessEqualThan_Left, null, null));
        // parseエラー
        try { CompareUtils.compareInCriteria(CompareCriteria.Year_GraterThan_Left, str1, year1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Year_GraterThan_Left, year1, "100"); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Year_GraterThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Year_LessThan_Left, str1, year1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Year_LessThan_Left, year1, "100"); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Year_LessThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Year_GraterEqualThan_Left, str1, year1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Year_GraterEqualThan_Left, year1, "100"); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Year_GraterEqualThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Year_LessEqualThan_Left, str1, year1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Year_LessEqualThan_Left, year1, "100"); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Year_LessEqualThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }

        //--------------------------------------------------------------------------------
        // 年月
        //--------------------------------------------------------------------------------
        String month1 = "201412";
        String month2 = "201501";
        String month3 = "201502";
        assertEquals("年月＜ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Month_GraterThan_Left, month2, month3));
        assertEquals("年月＜ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Month_GraterThan_Left, month2, month2));
        assertEquals("年月＜ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Month_GraterThan_Left, month2, month1));
        assertEquals("年月＞ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Month_LessThan_Left, month2, month1));
        assertEquals("年月＞ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Month_LessThan_Left, month2, month2));
        assertEquals("年月＞ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Month_LessThan_Left, month2, month3));
        assertEquals("年月＜＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Month_GraterEqualThan_Left, month1, month2));
        assertEquals("年月＜＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Month_GraterEqualThan_Left, month2, month2));
        assertEquals("年月＜＝ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Month_GraterEqualThan_Left, month3, month2));
        assertEquals("年月＞＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Month_LessEqualThan_Left, month3, month2));
        assertEquals("年月＞＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Month_LessEqualThan_Left, month2, month2));
        assertEquals("年月＞＝ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Month_LessEqualThan_Left, month1, month2));
        // nullチェック
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Month_GraterThan_Left, null, month1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Month_GraterThan_Left, month1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Month_GraterThan_Left, null, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Month_LessThan_Left, null, month1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Month_LessThan_Left, month1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Month_LessThan_Left, null, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Month_GraterEqualThan_Left, null, month1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Month_GraterEqualThan_Left, month1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Month_GraterEqualThan_Left, null, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Month_LessEqualThan_Left, null, month1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Month_LessEqualThan_Left, month1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Month_LessEqualThan_Left, null, null));
        // parseエラー
        try { CompareUtils.compareInCriteria(CompareCriteria.Month_GraterThan_Left, str1, month1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Month_GraterThan_Left, month1, "100"); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Month_GraterThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Month_LessThan_Left, str1, month1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Month_LessThan_Left, month1, "100"); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Month_LessThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Month_GraterEqualThan_Left, str1, month1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Month_GraterEqualThan_Left, month1, "100"); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Month_GraterEqualThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Month_LessEqualThan_Left, str1, month1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Month_LessEqualThan_Left, month1, "100"); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Month_LessEqualThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }

        //--------------------------------------------------------------------------------
        // 年月日
        //--------------------------------------------------------------------------------
        String date1 = "20141231";
        String date2 = "20150101";
        String date3 = "2015-01-02";
        assertEquals("年月日＜ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Date_GraterThan_Left, date2, date3));
        assertEquals("年月日＜ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Date_GraterThan_Left, date2, date2));
        assertEquals("年月日＜ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Date_GraterThan_Left, date2, date1));
        assertEquals("年月日＞ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Date_LessThan_Left, date2, date1));
        assertEquals("年月日＞ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Date_LessThan_Left, date2, date2));
        assertEquals("年月日＞ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Date_LessThan_Left, date2, date3));
        assertEquals("年月日＜＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Date_GraterEqualThan_Left, date1, date2));
        assertEquals("年月日＜＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Date_GraterEqualThan_Left, date2, date2));
        assertEquals("年月日＜＝ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Date_GraterEqualThan_Left, date3, date2));
        assertEquals("年月日＞＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Date_LessEqualThan_Left, date3, date2));
        assertEquals("年月日＞＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Date_LessEqualThan_Left, date2, date2));
        assertEquals("年月日＞＝ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Date_LessEqualThan_Left, date1, date2));
        // nullチェック
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Date_GraterThan_Left, null, date1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Date_GraterThan_Left, date1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Date_GraterThan_Left, null, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Date_LessThan_Left, null, date1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Date_LessThan_Left, date1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Date_LessThan_Left, null, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Date_GraterEqualThan_Left, null, date1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Date_GraterEqualThan_Left, date1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Date_GraterEqualThan_Left, null, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Date_LessEqualThan_Left, null, date1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Date_LessEqualThan_Left, date1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Date_LessEqualThan_Left, null, null));
        // parseエラー
        try { CompareUtils.compareInCriteria(CompareCriteria.Date_GraterThan_Left, str1, date1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Date_GraterThan_Left, date1, "100"); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Date_GraterThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Date_LessThan_Left, str1, date1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Date_LessThan_Left, date1, "100"); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Date_LessThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Date_GraterEqualThan_Left, str1, date1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Date_GraterEqualThan_Left, date1, "100"); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Date_GraterEqualThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Date_LessEqualThan_Left, str1, date1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Date_LessEqualThan_Left, date1, "100"); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Date_LessEqualThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }

        //--------------------------------------------------------------------------------
        // 日時
        //--------------------------------------------------------------------------------
        String datetime1 = "2014/12/31 23:59:59.999";
        String datetime2 = "2015/01/01";
        String datetime3 = "2015-01-01T00:00:00.001+0900";
        assertEquals("日時＜ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Datetime_GraterThan_Left, datetime2, datetime3));
        assertEquals("日時＜ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Datetime_GraterThan_Left, datetime2, datetime2));
        assertEquals("日時＜ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Datetime_GraterThan_Left, datetime2, datetime1));
        assertEquals("日時＞ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Datetime_LessThan_Left, datetime2, datetime1));
        assertEquals("日時＞ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Datetime_LessThan_Left, datetime2, datetime2));
        assertEquals("日時＞ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Datetime_LessThan_Left, datetime2, datetime3));
        assertEquals("日時＜＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Datetime_GraterEqualThan_Left, datetime1, datetime2));
        assertEquals("日時＜＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Datetime_GraterEqualThan_Left, datetime2, datetime2));
        assertEquals("日時＜＝ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Datetime_GraterEqualThan_Left, datetime3, datetime2));
        assertEquals("日時＞＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Datetime_LessEqualThan_Left, datetime3, datetime2));
        assertEquals("日時＜＝ OK", CompareStatus.OK, CompareUtils.compareInCriteria(CompareCriteria.Datetime_LessEqualThan_Left, datetime2, datetime2));
        assertEquals("日時＞＝ NG", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Datetime_LessEqualThan_Left, datetime1, datetime2));
        // nullチェック
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Datetime_GraterThan_Left, null, datetime1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Datetime_GraterThan_Left, datetime1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Datetime_GraterThan_Left, null, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Datetime_LessThan_Left, null, datetime1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Datetime_LessThan_Left, datetime1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Datetime_LessThan_Left, null, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Datetime_GraterEqualThan_Left, null, datetime1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Datetime_GraterEqualThan_Left, datetime1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Datetime_GraterEqualThan_Left, null, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Datetime_LessEqualThan_Left, null, datetime1));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Datetime_LessEqualThan_Left, datetime1, null));
        assertEquals("nullチェック", CompareStatus.NG, CompareUtils.compareInCriteria(CompareCriteria.Datetime_LessEqualThan_Left, null, null));
        // parseエラー
        try { CompareUtils.compareInCriteria(CompareCriteria.Datetime_GraterThan_Left, str1, datetime1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Datetime_GraterThan_Left, datetime1, "100"); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Datetime_GraterThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Datetime_LessThan_Left, str1, datetime1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Datetime_LessThan_Left, datetime1, "100"); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Datetime_LessThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Datetime_GraterEqualThan_Left, str1, datetime1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Datetime_GraterEqualThan_Left, datetime1, "100"); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Datetime_GraterEqualThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Datetime_LessEqualThan_Left, str1, datetime1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Datetime_LessEqualThan_Left, datetime1, "100"); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
        try { CompareUtils.compareInCriteria(CompareCriteria.Datetime_LessEqualThan_Left, str1, str1); fail(); } catch (Exception e) { log.debug(e.getMessage()); }
    }

}
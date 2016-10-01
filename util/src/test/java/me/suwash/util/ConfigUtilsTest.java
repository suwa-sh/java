package me.suwash.util;

import static org.junit.Assert.*;

import java.io.InputStreamReader;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

@lombok.extern.slf4j.Slf4j
public class ConfigUtilsTest {

    @Rule
    public TestName name = new TestName();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        log.debug("■■■ " + ConfigUtilsTest.class.getName() + " ■■■");
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
    public void testGetConfigFileClasspath() {
        Class<?> type;
        String ext;
        String expect;

        type = Object.class;
        ext = "txt";
        expect = "/object.txt";
        assertEquals("単語数＝1の場合", expect, ConfigUtils.getConfigFileClasspath(type, ext));

        type = RuntimeException.class;
        ext = "txt";
        expect = "/runtime_exception.txt";
        assertEquals("単語数＝2の場合", expect, ConfigUtils.getConfigFileClasspath(type, ext));

        type = InputStreamReader.class;
        ext = "txt";
        expect = "/input_stream_reader.txt";
        assertEquals("単語数＝3の場合", expect, ConfigUtils.getConfigFileClasspath(type, ext));

        type = SampleAAA.class;
        ext = "txt";
        expect = "/sample_a_a_a.txt";
        assertEquals("大文字が連続している場合", expect, ConfigUtils.getConfigFileClasspath(type, ext));

    }

    @Test
    public void testGetConfigFileClasspathWithPackage() {
        Class<?> type;
        String ext;
        String expect;

        type = Object.class;
        ext = "txt";
        expect = "/java/lang/object.txt";
        assertEquals("単語数＝1の場合", expect, ConfigUtils.getConfigFileClasspathWithPackage(type, ext));

        type = RuntimeException.class;
        ext = "txt";
        expect = "/java/lang/runtime_exception.txt";
        assertEquals("単語数＝2の場合", expect, ConfigUtils.getConfigFileClasspathWithPackage(type, ext));

        type = InputStreamReader.class;
        ext = "txt";
        expect = "/java/io/input_stream_reader.txt";
        assertEquals("単語数＝3の場合", expect, ConfigUtils.getConfigFileClasspathWithPackage(type, ext));

        type = SampleAAA.class;
        ext = "txt";
        expect = "/me/suwash/util/sample_a_a_a.txt";
        assertEquals("大文字が連続している場合", expect, ConfigUtils.getConfigFileClasspathWithPackage(type, ext));

    }

    private class SampleAAA {
    }

}

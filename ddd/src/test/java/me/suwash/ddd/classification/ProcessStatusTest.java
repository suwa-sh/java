package me.suwash.ddd.classification;

import static org.junit.Assert.assertEquals;
import me.suwash.test.DefaultTestWatcher;

import org.junit.Rule;
import org.junit.Test;

@lombok.extern.slf4j.Slf4j
public class ProcessStatusTest {

    @Rule
    public DefaultTestWatcher watcher = new DefaultTestWatcher();

    @Test
    public final void testDefaultValue() {
        ProcessStatus expected = ProcessStatus.Processing;
        ProcessStatus actual = ProcessStatus.defaultValue();

        assertEquals(expected, actual);
        log.debug(actual.toString());
    }

    @Test
    public final void testDefaultValueString() {
        ProcessStatus expected = ProcessStatus.Success;
        ProcessStatus actual = ProcessStatus.defaultValue(ProcessStatus.GROUP_FINISHED);

        assertEquals(expected, actual);
        log.debug(actual.toString());
    }

    @Test
    public final void testGroups() {
        String[] expected = {ProcessStatus.GROUP_DEFAULT, ProcessStatus.GROUP_FINISHED};
        String[] actual = ProcessStatus.groups();

        for(int i = 0; i < actual.length; i++) {
            assertEquals(expected[i], actual[i]);
            log.debug(actual[i].toString());
        }
    }

    @Test
    public final void testValues() {
        ProcessStatus[] expected = {ProcessStatus.Processing, ProcessStatus.Success, ProcessStatus.Warning, ProcessStatus.Failure};
        ProcessStatus[] actual = ProcessStatus.values(ProcessStatus.GROUP_DEFAULT);

        for(int i = 0; i < actual.length; i++) {
            assertEquals(expected[i], actual[i]);
            log.debug(actual[i].toString());
        }
    }

    @Test
    public final void testValueOf() {
        String name = "Processing";

        ProcessStatus expected = ProcessStatus.Processing;
        ProcessStatus actual = ProcessStatus.valueOf(name);

        assertEquals(expected, actual);
        log.debug(actual.toString());
    }

    @Test
    public final void testValueOfByDdId() {
        String ddId = "ProcessStatus.Processing";

        ProcessStatus expected = ProcessStatus.Processing;
        ProcessStatus actual = ProcessStatus.valueOfByDdId(ddId);

        assertEquals(expected, actual);
        log.debug(actual.toString());
    }

    @Test
    public final void testValueOfByStoreValue() {
        String storeValue = "PRC";

        ProcessStatus expected = ProcessStatus.Processing;
        ProcessStatus actual = ProcessStatus.valueOfByStoreValue(storeValue);

        assertEquals(expected, actual);
        log.debug(actual.toString());
    }

    @Test
    public final void testContainsNameString() {
        String name = "Processing";

        boolean expected = true;
        boolean actual = ProcessStatus.containsName(name);

        assertEquals(expected, actual);
        log.debug("" + actual);
    }

    @Test
    public final void testContainsNameStringString() {
        String group = ProcessStatus.GROUP_FINISHED;
        String name = "Processing";

        boolean expected = false;
        boolean actual = ProcessStatus.containsName(group, name);

        assertEquals(expected, actual);
        log.debug("" + actual);
    }

    @Test
    public final void testContainsDdIdString() {
        String ddId = "ProcessStatus.Processing";

        boolean expected = true;
        boolean actual = ProcessStatus.containsDdId(ddId);

        assertEquals(expected, actual);
        log.debug("" + actual);
    }

    @Test
    public final void testContainsDdIdStringString() {
        String group = ProcessStatus.GROUP_FINISHED;
        String ddId = "ProcessStatus.Processing";

        boolean expected = false;
        boolean actual = ProcessStatus.containsDdId(group, ddId);

        assertEquals(expected, actual);
        log.debug("" + actual);
    }

    @Test
    public final void testContainsStoreValueString() {
        String storeValue = "PRC";

        boolean expected = true;
        boolean actual = ProcessStatus.containsStoreValue(storeValue);

        assertEquals(expected, actual);
        log.debug("" + actual);
    }

    @Test
    public final void testContainsStoreValueStringString() {
        String group = ProcessStatus.GROUP_FINISHED;
        String storeValue = "PRC";

        boolean expected = false;
        boolean actual = ProcessStatus.containsStoreValue(group, storeValue);

        assertEquals(expected, actual);
        log.debug("" + actual);
    }

    @Test
    public final void testDdId() {
        String expected = "ProcessStatus.Processing";
        String actual = ProcessStatus.Processing.ddId();

        assertEquals(expected, actual);
        log.debug(actual);
    }

    @Test
    public final void testStoreValue() {
        String expected = "PRC";
        String actual = ProcessStatus.Processing.storeValue();

        assertEquals(expected, actual);
        log.debug(actual);
    }

    @Test
    public final void testToString() {
        String expected = "ProcessStatus.Processing(PRC)";
        String actual = ProcessStatus.Processing.toString();

        assertEquals(expected, actual);
        log.debug(actual);
    }

}

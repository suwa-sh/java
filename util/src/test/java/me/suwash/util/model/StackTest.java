package me.suwash.util.model;

import static org.junit.Assert.*;
import me.suwash.util.RuntimeUtils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

@lombok.extern.slf4j.Slf4j
public class StackTest {

    @Rule
    public TestName name = new TestName();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        log.debug("■■■ " + StackTest.class.getName() + " ■■■");
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
        String elem1 = "elem1";
        String elem2 = "elem2";
        String elem3 = "elem3";

        Stack<String> stack = new Stack<String>();

        assertTrue("未登録の場合、trueを返すこと", stack.isEmpty());
        assertEquals("未登録の場合、要素0件のリストを返すこと", 0, stack.elementList().size());
        assertNull("未登録の場合、nullを返すこと", stack.peek());
        assertNull("未登録の場合、nullを返すこと", stack.pop());
        log.debug("stack      : " + stack.toString());
        log.debug("elementList: " + stack.elementList().toString());

        stack.push(elem1);
        assertFalse("登録済の場合、falseを返すこと", stack.isEmpty());
        assertEquals("登録件数の要素を持つリストを返すこと", 1, stack.elementList().size());
        log.debug("stack      : " + stack.toString());
        log.debug("elementList: " + stack.elementList().toString());

        stack.push(elem2);
        assertFalse("登録済の場合、falseを返すこと", stack.isEmpty());
        assertEquals("登録件数の要素を持つリストを返すこと", 2, stack.elementList().size());
        log.debug("stack      : " + stack.toString());
        log.debug("elementList: " + stack.elementList().toString());

        stack.push(elem3);
        assertFalse("登録済の場合、falseを返すこと", stack.isEmpty());
        assertEquals("登録件数の要素を持つリストを返すこと", 3, stack.elementList().size());
        log.debug("stack      : " + stack.toString());
        log.debug("elementList: " + stack.elementList().toString());

        assertEquals("最後に登録した要素を返すこと", elem3, stack.peek());
        assertEquals("最後に登録した要素を返すこと", elem3, stack.pop());
        assertFalse("登録済の場合、falseを返すこと", stack.isEmpty());
        assertEquals("要素が減っていること", 2, stack.elementList().size());
        log.debug("stack      : " + stack.toString());
        log.debug("elementList: " + stack.elementList().toString());

        assertEquals("一つ前に登録した要素を返すこと", elem2, stack.peek());
        assertEquals("一つ前に登録した要素を返すこと", elem2, stack.pop());
        assertFalse("登録済の場合、falseを返すこと", stack.isEmpty());
        assertEquals("要素が減っていること", 1, stack.elementList().size());
        log.debug("stack      : " + stack.toString());
        log.debug("elementList: " + stack.elementList().toString());

        assertEquals("一つ前に登録した要素を返すこと", elem1, stack.peek());
        assertEquals("一つ前に登録した要素を返すこと", elem1, stack.pop());
        assertTrue("未登録の場合、trueを返すこと", stack.isEmpty());
        assertEquals("要素が減っていること", 0, stack.elementList().size());
        log.debug("stack      : " + stack.toString());
        log.debug("elementList: " + stack.elementList().toString());

        assertNull("全ての要素を取り出した場合、nullを返すこと", stack.peek());
        assertNull("全ての要素を取り出した場合、nullを返すこと", stack.pop());
    }

}

package me.suwash.util.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import me.suwash.test.DefaultTestWatcher;
import me.suwash.util.test.UtilTestWatcher;

import org.junit.Rule;
import org.junit.Test;

@lombok.extern.slf4j.Slf4j
public class StackTest {

    @Rule
    public DefaultTestWatcher watcher = new UtilTestWatcher();

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

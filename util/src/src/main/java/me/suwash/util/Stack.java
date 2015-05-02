package me.suwash.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * LIFOモデルのデータ構造。
 *
 * @param <E> 格納する型
 */
public class Stack<E> {

    /** Dequeをコンテンツとして保持 */
    private Deque<E> deque;

    /**
     * コンストラクタ。
     */
    public Stack() {
        deque = new ArrayDeque<E>();
    }

    /**
     * 要素を追加します。
     *
     * @param element 要素
     * @return 要素の追加に成功した場合、true
     */
    public boolean push(E element) {
        return deque.offerFirst(element);
    }

    /**
     * 最後に追加した要素を取り出し、保持内容から削除します。
     *
     * @return 最後に追加した要素
     */
    public E pop() {
        return deque.pollFirst();
    }

    /**
     * 最後に追加した要素します。
     * 保持内容は変化しません。
     *
     * @return 最後に追加した要素
     */
    public E peek() {
        return deque.peekFirst();
    }

    public boolean isEmpty() {
        return deque.isEmpty();
    }

    public List<E> elementList() {
        List<E> elementList = new ArrayList<E>();
        for (E element : deque) {
            elementList.add(element);
        }
        return elementList;
    }

    @Override
    public String toString() {
        return deque.toString();
    }
}

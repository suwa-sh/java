package me.suwash.util.classification;


/**
 * 区分インタフェース。
 * enumに実装することを前提にしています。
 *
 * ・Category(区分)
 * 　・Cat1(区分値1)
 * 　　・name: Cat1
 * 　　・ddId: Category.Cat1
 * 　　・storeValue: "ONE"
 * 　・Cat2(区分値2)
 * 　　・name: Cat2
 * 　　・ddId: Category.Cat2
 * 　　・storeValue: "TWO"
 */
public interface Classification {

    /**
     * 処理で利用する区分値名を返します。
     *
     * @return 区分値名
     */
    abstract public String name();

    /**
     * 表示に利用するデータディクショナリIDを返します。
     *
     * @return データディクショナリID
     */
    abstract public String ddId();

    /**
     * 永続化に利用する値を返します。
     *
     * @return 永続化値
     */
    abstract public String storeValue();
}

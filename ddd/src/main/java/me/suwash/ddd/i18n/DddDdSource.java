package me.suwash.ddd.i18n;

import me.suwash.util.i18n.DdSource;

/**
 * データディクショナリ用プロパティファイルの定義保持クラス。
 */
public class DddDdSource extends DdSource {
    private static DddDdSource instance = new DddDdSource();

    /**
     * Singletonパターンでインスタンスを返します。
     *
     * @return インスタンス
     */
    public static DddDdSource getInstance() {
        return instance;
    }

    /*
     * (非 Javadoc)
     * @see me.suwash.util.i18n.DdSource#getParent()
     */
    @Override
    protected DdSource getParent() {
        return DdSource.getInstance();
    }
}

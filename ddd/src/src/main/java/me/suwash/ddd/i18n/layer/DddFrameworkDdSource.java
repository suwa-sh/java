package me.suwash.ddd.i18n.layer;

import me.suwash.util.i18n.DdSource;

/**
 * データディクショナリ用プロパティファイルの定義保持クラス。
 */
public class DddFrameworkDdSource extends DdSource {
    private static DddFrameworkDdSource instance = new DddFrameworkDdSource();

    /**
     * Singletonパターンでオブジェクトを返します。
     * @return DataDictionaryオブジェクト
     */
    public static DddFrameworkDdSource getInstance() {
        return instance;
    }

    @Override
    protected DdSource getParent() {
        return DdSource.getInstance();
    }
}

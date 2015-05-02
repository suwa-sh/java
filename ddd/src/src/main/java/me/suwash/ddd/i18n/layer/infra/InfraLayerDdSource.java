package me.suwash.ddd.i18n.layer.infra;

import me.suwash.util.i18n.DdSource;

/**
 * データディクショナリ用プロパティファイルの定義保持クラス。
 */
public class InfraLayerDdSource extends DdSource {
    private static InfraLayerDdSource instance = new InfraLayerDdSource();

    /**
     * Singletonパターンでオブジェクトを返します。
     * @return DataDictionaryオブジェクト
     */
    public static InfraLayerDdSource getInstance() {
        return instance;
    }

    @Override
    protected DdSource getParent() {
        return DdSource.getInstance();
    }
}

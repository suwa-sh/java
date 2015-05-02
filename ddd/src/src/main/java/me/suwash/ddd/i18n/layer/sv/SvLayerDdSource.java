package me.suwash.ddd.i18n.layer.sv;

import me.suwash.ddd.i18n.layer.infra.InfraLayerDdSource;
import me.suwash.util.i18n.DdSource;

/**
 * データディクショナリ用プロパティファイルの定義保持クラス。
 */
public class SvLayerDdSource extends DdSource {
    private static SvLayerDdSource instance = new SvLayerDdSource();

    /**
     * Singletonパターンでオブジェクトを返します。
     * @return DataDictionaryオブジェクト
     */
    public static SvLayerDdSource getInstance() {
        return instance;
    }

    @Override
    protected DdSource getParent() {
        return InfraLayerDdSource.getInstance();
    }
}

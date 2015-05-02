package me.suwash.ddd.i18n.layer.da;

import me.suwash.ddd.i18n.layer.sv.SvLayerDdSource;
import me.suwash.util.i18n.DdSource;

/**
 * データディクショナリ用プロパティファイルの定義保持クラス。
 */
public class DaLayerDdSource extends DdSource {
    private static DaLayerDdSource instance = new DaLayerDdSource();

    /**
     * Singletonパターンでオブジェクトを返します。
     * @return DataDictionaryオブジェクト
     */
    public static DaLayerDdSource getInstance() {
        return instance;
    }

    @Override
    protected DdSource getParent() {
        return SvLayerDdSource.getInstance();
    }
}

package me.suwash.ddd.i18n.layer.ap;

import me.suwash.ddd.i18n.layer.sv.SvLayerDdSource;
import me.suwash.util.i18n.DdSource;

/**
 * データディクショナリ用プロパティファイルの定義保持クラス。
 */
public class ApLayerDdSource extends DdSource {
    private static ApLayerDdSource instance = new ApLayerDdSource();

    /**
     * Singletonパターンでオブジェクトを返します。
     * @return DataDictionaryオブジェクト
     */
    public static ApLayerDdSource getInstance() {
        return instance;
    }

    @Override
    protected DdSource getParent() {
        return SvLayerDdSource.getInstance();
    }
}

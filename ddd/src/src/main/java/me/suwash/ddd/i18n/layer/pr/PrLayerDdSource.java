package me.suwash.ddd.i18n.layer.pr;

import me.suwash.ddd.i18n.layer.ap.ApLayerDdSource;
import me.suwash.util.i18n.DdSource;

/**
 * データディクショナリ用プロパティファイルの定義保持クラス。
 */
public class PrLayerDdSource extends DdSource {
    private static PrLayerDdSource instance = new PrLayerDdSource();

    /**
     * Singletonパターンでオブジェクトを返します。
     * @return DataDictionaryオブジェクト
     */
    public static PrLayerDdSource getInstance() {
        return instance;
    }

    @Override
    protected DdSource getParent() {
        return ApLayerDdSource.getInstance();
    }
}

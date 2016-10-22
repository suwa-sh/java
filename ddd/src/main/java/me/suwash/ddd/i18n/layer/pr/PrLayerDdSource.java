package me.suwash.ddd.i18n.layer.pr;

import me.suwash.ddd.i18n.layer.ap.ApLayerDdSource;
import me.suwash.util.i18n.DdSource;

/**
 * データディクショナリ用プロパティファイルの定義保持クラス。
 */
public class PrLayerDdSource extends DdSource {
    private static PrLayerDdSource instance = new PrLayerDdSource();

    /**
     * Singletonパターンでインスタンスを返します。
     *
     * @return インスタンス
     */
    public static PrLayerDdSource getInstance() {
        return instance;
    }

    /*
     * (非 Javadoc)
     * @see me.suwash.util.i18n.DdSource#getParent()
     */
    @Override
    protected DdSource getParent() {
        return ApLayerDdSource.getInstance();
    }
}

package me.suwash.ddd.i18n.layer.ap;

import me.suwash.ddd.i18n.layer.sv.SvLayerDdSource;
import me.suwash.util.i18n.DdSource;

/**
 * データディクショナリ用プロパティファイルの定義保持クラス。
 */
public class ApLayerDdSource extends DdSource {
    private static ApLayerDdSource instance = new ApLayerDdSource();

    /**
     * Singletonパターンでインスタンスを返します。
     *
     * @return インスタンス
     */
    public static ApLayerDdSource getInstance() {
        return instance;
    }

    /*
     * (非 Javadoc)
     * @see me.suwash.util.i18n.DdSource#getParent()
     */
    @Override
    protected DdSource getParent() {
        return SvLayerDdSource.getInstance();
    }
}

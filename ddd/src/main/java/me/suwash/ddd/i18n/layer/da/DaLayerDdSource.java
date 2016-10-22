package me.suwash.ddd.i18n.layer.da;

import me.suwash.ddd.i18n.layer.sv.SvLayerDdSource;
import me.suwash.util.i18n.DdSource;

/**
 * データディクショナリ用プロパティファイルの定義保持クラス。
 */
public class DaLayerDdSource extends DdSource {
    private static DaLayerDdSource instance = new DaLayerDdSource();

    /**
     * Singletonパターンでインスタンスを返します。
     *
     * @return インスタンス
     */
    public static DaLayerDdSource getInstance() {
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

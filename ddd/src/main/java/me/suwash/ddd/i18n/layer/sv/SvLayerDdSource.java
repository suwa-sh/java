package me.suwash.ddd.i18n.layer.sv;

import me.suwash.ddd.i18n.layer.infra.InfraLayerDdSource;
import me.suwash.util.i18n.DdSource;

/**
 * データディクショナリ用プロパティファイルの定義保持クラス。
 */
public class SvLayerDdSource extends DdSource {
    private static SvLayerDdSource instance = new SvLayerDdSource();

    /**
     * Singletonパターンでインスタンスを返します。
     *
     * @return インスタンス
     */
    public static SvLayerDdSource getInstance() {
        return instance;
    }

    /*
     * (非 Javadoc)
     * @see me.suwash.util.i18n.DdSource#getParent()
     */
    @Override
    protected DdSource getParent() {
        return InfraLayerDdSource.getInstance();
    }
}

package me.suwash.ddd.i18n.layer.infra;

import me.suwash.ddd.i18n.DddDdSource;
import me.suwash.util.i18n.DdSource;

/**
 * データディクショナリ用プロパティファイルの定義保持クラス。
 */
public class InfraLayerDdSource extends DdSource {
    private static InfraLayerDdSource instance = new InfraLayerDdSource();

    /**
     * Singletonパターンでインスタンスを返します。
     *
     * @return インスタンス
     */
    public static InfraLayerDdSource getInstance() {
        return instance;
    }

    /*
     * (非 Javadoc)
     * @see me.suwash.util.i18n.DdSource#getParent()
     */
    @Override
    protected DdSource getParent() {
        return DddDdSource.getInstance();
    }
}

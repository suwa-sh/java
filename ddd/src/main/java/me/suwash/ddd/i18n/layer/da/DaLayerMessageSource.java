package me.suwash.ddd.i18n.layer.da;

import me.suwash.ddd.i18n.layer.sv.SvLayerMessageSource;
import me.suwash.util.i18n.DdSource;
import me.suwash.util.i18n.MessageSource;

/**
 * メッセージ用プロパティファイルの定義保持クラス。
 */
public class DaLayerMessageSource extends MessageSource {
    private static DaLayerMessageSource instance = new DaLayerMessageSource();

    /**
     * Singletonパターンでインスタンスを返します。
     *
     * @return インスタンス
     */
    public static DaLayerMessageSource getInstance() {
        return instance;
    }

    /*
     * (非 Javadoc)
     * @see me.suwash.util.i18n.MessageSource#getParent()
     */
    @Override
    protected MessageSource getParent() {
        return SvLayerMessageSource.getInstance();
    }

    /*
     * (非 Javadoc)
     * @see me.suwash.util.i18n.MessageSource#getDd()
     */
    @Override
    protected DdSource getDd() {
        return DaLayerDdSource.getInstance();
    }
}

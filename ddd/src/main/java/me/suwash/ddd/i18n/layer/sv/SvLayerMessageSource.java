package me.suwash.ddd.i18n.layer.sv;

import me.suwash.ddd.i18n.layer.infra.InfraLayerMessageSource;
import me.suwash.util.i18n.DdSource;
import me.suwash.util.i18n.MessageSource;

/**
 * メッセージ用プロパティファイルの定義保持クラス。
 */
public class SvLayerMessageSource extends MessageSource {
    private static SvLayerMessageSource instance = new SvLayerMessageSource();

    /**
     * Singletonパターンでインスタンスを返します。
     *
     * @return インスタンス
     */
    public static SvLayerMessageSource getInstance() {
        return instance;
    }

    /*
     * (非 Javadoc)
     * @see me.suwash.util.i18n.MessageSource#getParent()
     */
    @Override
    protected MessageSource getParent() {
        return InfraLayerMessageSource.getInstance();
    }

    /*
     * (非 Javadoc)
     * @see me.suwash.util.i18n.MessageSource#getDd()
     */
    @Override
    protected DdSource getDd() {
        return SvLayerDdSource.getInstance();
    }
}

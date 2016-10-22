package me.suwash.ddd.i18n.layer.infra;

import me.suwash.ddd.i18n.DddMessageSource;
import me.suwash.util.i18n.DdSource;
import me.suwash.util.i18n.MessageSource;

/**
 * メッセージ用プロパティファイルの定義保持クラス。
 */
public class InfraLayerMessageSource extends MessageSource {
    private static InfraLayerMessageSource instance = new InfraLayerMessageSource();

    /**
     * Singletonパターンでインスタンスを返します。
     *
     * @return インスタンス
     */
    public static InfraLayerMessageSource getInstance() {
        return instance;
    }

    /*
     * (非 Javadoc)
     * @see me.suwash.util.i18n.MessageSource#getParent()
     */
    @Override
    protected MessageSource getParent() {
        return DddMessageSource.getInstance();
    }

    /*
     * (非 Javadoc)
     * @see me.suwash.util.i18n.MessageSource#getDd()
     */
    @Override
    protected DdSource getDd() {
        return InfraLayerDdSource.getInstance();
    }
}

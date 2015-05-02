package me.suwash.ddd.i18n.layer.infra;

import me.suwash.util.i18n.DdSource;
import me.suwash.util.i18n.MessageSource;

/**
 * メッセージ用プロパティファイルの定義保持クラス。
 */
public class InfraLayerMessageSource extends MessageSource {
    private static InfraLayerMessageSource instance = new InfraLayerMessageSource();

    /**
     * Singletonパターンでオブジェクトを返します。
     * @return Messageオブジェクト
     */
    public static InfraLayerMessageSource getInstance() {
        return instance;
    }

    @Override
    protected MessageSource getParent() {
        return MessageSource.getInstance();
    }

    @Override
    protected DdSource getDd() {
        return InfraLayerDdSource.getInstance();
    }
}

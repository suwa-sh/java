package me.suwash.ddd.i18n.layer.pr;

import me.suwash.ddd.i18n.layer.ap.ApLayerMessageSource;
import me.suwash.util.i18n.DdSource;
import me.suwash.util.i18n.MessageSource;

/**
 * メッセージ用プロパティファイルの定義保持クラス。
 */
public class PrLayerMessageSource extends MessageSource {
    private static PrLayerMessageSource instance = new PrLayerMessageSource();

    /**
     * Singletonパターンでオブジェクトを返します。
     * @return Messageオブジェクト
     */
    public static PrLayerMessageSource getInstance() {
        return instance;
    }

    @Override
    protected MessageSource getParent() {
        return ApLayerMessageSource.getInstance();
    }

    @Override
    protected DdSource getDd() {
        return PrLayerDdSource.getInstance();
    }
}

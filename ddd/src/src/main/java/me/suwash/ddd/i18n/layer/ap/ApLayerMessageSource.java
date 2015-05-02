package me.suwash.ddd.i18n.layer.ap;

import me.suwash.ddd.i18n.layer.sv.SvLayerMessageSource;
import me.suwash.util.i18n.DdSource;
import me.suwash.util.i18n.MessageSource;

/**
 * メッセージ用プロパティファイルの定義保持クラス。
 */
public class ApLayerMessageSource extends MessageSource {
    private static ApLayerMessageSource instance = new ApLayerMessageSource();

    /**
     * Singletonパターンでオブジェクトを返します。
     * @return Messageオブジェクト
     */
    public static ApLayerMessageSource getInstance() {
        return instance;
    }

    @Override
    protected MessageSource getParent() {
        return SvLayerMessageSource.getInstance();
    }

    @Override
    protected DdSource getDd() {
        return ApLayerDdSource.getInstance();
    }
}

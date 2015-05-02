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
     * Singletonパターンでオブジェクトを返します。
     * @return Messageオブジェクト
     */
    public static SvLayerMessageSource getInstance() {
        return instance;
    }

    @Override
    protected MessageSource getParent() {
        return InfraLayerMessageSource.getInstance();
    }

    @Override
    protected DdSource getDd() {
        return SvLayerDdSource.getInstance();
    }
}

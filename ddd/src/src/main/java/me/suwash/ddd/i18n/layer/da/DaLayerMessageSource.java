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
     * Singletonパターンでオブジェクトを返します。
     * @return Messageオブジェクト
     */
    public static DaLayerMessageSource getInstance() {
        return instance;
    }

    @Override
    protected MessageSource getParent() {
        return SvLayerMessageSource.getInstance();
    }

    @Override
    protected DdSource getDd() {
        return DaLayerDdSource.getInstance();
    }
}

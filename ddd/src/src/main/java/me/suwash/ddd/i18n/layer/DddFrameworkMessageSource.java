package me.suwash.ddd.i18n.layer;

import me.suwash.util.i18n.DdSource;
import me.suwash.util.i18n.MessageSource;

/**
 * メッセージ用プロパティファイルの定義保持クラス。
 */
public class DddFrameworkMessageSource extends MessageSource {
    private static DddFrameworkMessageSource instance = new DddFrameworkMessageSource();

    /**
     * Singletonパターンでオブジェクトを返します。
     * @return Messageオブジェクト
     */
    public static DddFrameworkMessageSource getInstance() {
        return instance;
    }

    @Override
    protected MessageSource getParent() {
        return MessageSource.getInstance();
    }

    @Override
    protected DdSource getDd() {
        return DddFrameworkDdSource.getInstance();
    }
}

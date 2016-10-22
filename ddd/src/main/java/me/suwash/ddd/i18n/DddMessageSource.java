package me.suwash.ddd.i18n;

import me.suwash.util.i18n.DdSource;
import me.suwash.util.i18n.MessageSource;

/**
 * メッセージ用プロパティファイルの定義保持クラス。
 */
public class DddMessageSource extends MessageSource {
    private static DddMessageSource instance = new DddMessageSource();

    /**
     * Singletonパターンでインスタンスを返します。
     *
     * @return インスタンス
     */
    public static DddMessageSource getInstance() {
        return instance;
    }

    /*
     * (非 Javadoc)
     * @see me.suwash.util.i18n.MessageSource#getParent()
     */
    @Override
    protected MessageSource getParent() {
        return MessageSource.getInstance();
    }

    /*
     * (非 Javadoc)
     * @see me.suwash.util.i18n.MessageSource#getDd()
     */
    @Override
    protected DdSource getDd() {
        return DddDdSource.getInstance();
    }
}

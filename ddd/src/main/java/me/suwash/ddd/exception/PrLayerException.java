package me.suwash.ddd.exception;

import java.util.Locale;

import me.suwash.ddd.i18n.layer.pr.PrLayerMessageSource;
import me.suwash.util.exception.LayerException;
import me.suwash.util.i18n.MessageSource;

/**
 * アプリケーション層の例外クラス。
 */
public class PrLayerException extends LayerException {

    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ。
     *
     * @param messageId メッセージID
     */
    public PrLayerException(String messageId) {
        super(messageId);
    }

    /**
     * コンストラクタ。
     *
     * @param messageId メッセージID
     * @param locale ロケール
     */
    public PrLayerException(String messageId, Locale locale) {
        super(messageId, locale);
    }

    /**
     * コンストラクタ。
     *
     * @param messageId メッセージID
     * @param messageArgs メッセージ引数配列
     */
    public PrLayerException(String messageId, Object[] messageArgs) {
        super(messageId, messageArgs);
    }

    /**
     * コンストラクタ。
     *
     * @param messageId メッセージID
     * @param messageArgs メッセージ引数配列
     * @param locale ロケール
     */
    public PrLayerException(String messageId, Object[] messageArgs, Locale locale) {
        super(messageId, messageArgs, locale);
    }

    /**
     * コンストラクタ。
     *
     * @param messageId メッセージID
     * @param cause 原因となった例外オブジェクト
     */
    public PrLayerException(String messageId, Throwable cause) {
        super(messageId, cause);
    }

    /**
     * コンストラクタ。
     *
     * @param messageId メッセージID
     * @param locale ロケール
     * @param cause 原因となった例外オブジェクト
     */
    public PrLayerException(String messageId, Locale locale, Throwable cause) {
        super(messageId, locale, cause);
    }

    /**
     * コンストラクタ。
     *
     * @param messageId メッセージID
     * @param messageArgs メッセージ引数配列
     * @param cause 原因となった例外オブジェクト
     */
    public PrLayerException(String messageId, Object[] messageArgs, Throwable cause) {
        super(messageId, messageArgs, cause);
    }

    /**
     * コンストラクタ。
     *
     * @param messageId メッセージID
     * @param messageArgs メッセージ引数配列
     * @param locale ロケール
     * @param cause 原因となった例外オブジェクト
     */
    public PrLayerException(String messageId, Object[] messageArgs, Locale locale, Throwable cause) {
        super(messageId, messageArgs, locale, cause);
    }

    /*
     * (非 Javadoc)
     * @see me.suwash.util.exception.LayerException#getMessageSource()
     */
    @Override
    protected MessageSource getMessageSource() {
        return PrLayerMessageSource.getInstance();
    }
}

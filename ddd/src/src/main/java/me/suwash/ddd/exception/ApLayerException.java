package me.suwash.ddd.exception;

import java.util.Locale;

import me.suwash.ddd.i18n.layer.ap.ApLayerMessageSource;
import me.suwash.util.exception.LayerException;
import me.suwash.util.i18n.MessageSource;

/**
 * アプリケーション層の例外クラス。
 */
public class ApLayerException extends LayerException {

    private static final long serialVersionUID = 1L;

    public ApLayerException(String messageId) {
        super(messageId);
    }

    public ApLayerException(String messageId, Locale locale) {
        super(messageId, locale);
    }

    public ApLayerException(String messageId, Object[] messageArgs) {
        super(messageId, messageArgs);
    }

    public ApLayerException(String messageId, Object[] messageArgs, Locale locale) {
        super(messageId, messageArgs, locale);
    }

    public ApLayerException(String messageId, Throwable cause) {
        super(messageId, cause);
    }

    public ApLayerException(String messageId, Locale locale, Throwable cause) {
        super(messageId, locale, cause);
    }

    public ApLayerException(String messageId, Object[] messageArgs, Throwable cause) {
        super(messageId, messageArgs, cause);
    }

    public ApLayerException(String messageId, Object[] messageArgs, Locale locale, Throwable cause) {
        super(messageId, messageArgs, locale, cause);
    }

    @Override
    protected MessageSource getMessageSource() {
        return ApLayerMessageSource.getInstance();
    }
}

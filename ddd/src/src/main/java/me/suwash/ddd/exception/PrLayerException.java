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

    public PrLayerException(String messageId) {
        super(messageId);
    }

    public PrLayerException(String messageId, Locale locale) {
        super(messageId, locale);
    }

    public PrLayerException(String messageId, Object[] messageArgs) {
        super(messageId, messageArgs);
    }

    public PrLayerException(String messageId, Object[] messageArgs, Locale locale) {
        super(messageId, messageArgs, locale);
    }

    public PrLayerException(String messageId, Throwable cause) {
        super(messageId, cause);
    }

    public PrLayerException(String messageId, Locale locale, Throwable cause) {
        super(messageId, locale, cause);
    }

    public PrLayerException(String messageId, Object[] messageArgs, Throwable cause) {
        super(messageId, messageArgs, cause);
    }

    public PrLayerException(String messageId, Object[] messageArgs, Locale locale, Throwable cause) {
        super(messageId, messageArgs, locale, cause);
    }

    @Override
    protected MessageSource getMessageSource() {
        return PrLayerMessageSource.getInstance();
    }
}

package me.suwash.ddd.exception;

import java.util.Locale;

import me.suwash.ddd.i18n.layer.da.DaLayerMessageSource;
import me.suwash.util.exception.LayerException;
import me.suwash.util.i18n.MessageSource;

/**
 * データアクセス層の例外クラス。
 */
public class DaLayerException extends LayerException {

    private static final long serialVersionUID = 1L;

    public DaLayerException(String messageId) {
        super(messageId);
    }

    public DaLayerException(String messageId, Locale locale) {
        super(messageId, locale);
    }

    public DaLayerException(String messageId, Object[] messageArgs) {
        super(messageId, messageArgs);
    }

    public DaLayerException(String messageId, Object[] messageArgs, Locale locale) {
        super(messageId, messageArgs, locale);
    }

    public DaLayerException(String messageId, Throwable cause) {
        super(messageId, cause);
    }

    public DaLayerException(String messageId, Locale locale, Throwable cause) {
        super(messageId, locale, cause);
    }

    public DaLayerException(String messageId, Object[] messageArgs, Throwable cause) {
        super(messageId, messageArgs, cause);
    }

    public DaLayerException(String messageId, Object[] messageArgs, Locale locale, Throwable cause) {
        super(messageId, messageArgs, locale, cause);
    }

    @Override
    protected MessageSource getMessageSource() {
        return DaLayerMessageSource.getInstance();
    }
}

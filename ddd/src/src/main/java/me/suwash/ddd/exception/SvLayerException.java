package me.suwash.ddd.exception;

import java.util.Locale;

import me.suwash.ddd.i18n.layer.sv.SvLayerMessageSource;
import me.suwash.util.exception.LayerException;
import me.suwash.util.i18n.MessageSource;

/**
 * サービス層の例外クラス。
 */
public class SvLayerException extends LayerException {

    private static final long serialVersionUID = 1L;

    public SvLayerException(String messageId) {
        super(messageId);
    }

    public SvLayerException(String messageId, Locale locale) {
        super(messageId, locale);
    }

    public SvLayerException(String messageId, Object[] messageArgs) {
        super(messageId, messageArgs);
    }

    public SvLayerException(String messageId, Object[] messageArgs, Locale locale) {
        super(messageId, messageArgs, locale);
    }

    public SvLayerException(String messageId, Throwable cause) {
        super(messageId, cause);
    }

    public SvLayerException(String messageId, Locale locale, Throwable cause) {
        super(messageId, locale, cause);
    }

    public SvLayerException(String messageId, Object[] messageArgs, Throwable cause) {
        super(messageId, messageArgs, cause);
    }

    public SvLayerException(String messageId, Object[] messageArgs, Locale locale, Throwable cause) {
        super(messageId, messageArgs, locale, cause);
    }

    @Override
    protected MessageSource getMessageSource() {
        return SvLayerMessageSource.getInstance();
    }
}

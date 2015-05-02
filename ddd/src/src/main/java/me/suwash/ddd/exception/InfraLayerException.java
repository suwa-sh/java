package me.suwash.ddd.exception;

import java.util.Locale;

import me.suwash.ddd.i18n.layer.infra.InfraLayerMessageSource;
import me.suwash.util.exception.LayerException;
import me.suwash.util.i18n.MessageSource;

/**
 * インフラ層の例外クラス。
 */
public class InfraLayerException extends LayerException {

    private static final long serialVersionUID = 1L;

    public InfraLayerException(String messageId) {
        super(messageId);
    }

    public InfraLayerException(String messageId, Locale locale) {
        super(messageId, locale);
    }

    public InfraLayerException(String messageId, Object[] messageArgs) {
        super(messageId, messageArgs);
    }

    public InfraLayerException(String messageId, Object[] messageArgs, Locale locale) {
        super(messageId, messageArgs, locale);
    }

    public InfraLayerException(String messageId, Throwable cause) {
        super(messageId, cause);
    }

    public InfraLayerException(String messageId, Locale locale, Throwable cause) {
        super(messageId, locale, cause);
    }

    public InfraLayerException(String messageId, Object[] messageArgs, Throwable cause) {
        super(messageId, messageArgs, cause);
    }

    public InfraLayerException(String messageId, Object[] messageArgs, Locale locale, Throwable cause) {
        super(messageId, messageArgs, locale, cause);
    }

    @Override
    protected MessageSource getMessageSource() {
        return InfraLayerMessageSource.getInstance();
    }
}

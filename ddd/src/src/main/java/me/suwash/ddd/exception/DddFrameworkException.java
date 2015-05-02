package me.suwash.ddd.exception;

import java.util.Locale;

import me.suwash.ddd.i18n.layer.DddFrameworkMessageSource;
import me.suwash.util.exception.LayerException;
import me.suwash.util.i18n.MessageSource;

/**
 * DDDフレームワークの例外クラス。
 */
public class DddFrameworkException extends LayerException {

    private static final long serialVersionUID = 1L;

    public DddFrameworkException(String messageId) {
        super(messageId);
    }

    public DddFrameworkException(String messageId, Locale locale) {
        super(messageId, locale);
    }

    public DddFrameworkException(String messageId, Object[] messageArgs) {
        super(messageId, messageArgs);
    }

    public DddFrameworkException(String messageId, Object[] messageArgs, Locale locale) {
        super(messageId, messageArgs, locale);
    }

    public DddFrameworkException(String messageId, Throwable cause) {
        super(messageId, cause);
    }

    public DddFrameworkException(String messageId, Locale locale, Throwable cause) {
        super(messageId, locale, cause);
    }

    public DddFrameworkException(String messageId, Object[] messageArgs, Throwable cause) {
        super(messageId, messageArgs, cause);
    }

    public DddFrameworkException(String messageId, Object[] messageArgs, Locale locale, Throwable cause) {
        super(messageId, messageArgs, locale, cause);
    }

    @Override
    protected MessageSource getMessageSource() {
        return DddFrameworkMessageSource.getInstance();
    }
}

package me.suwash.util.exception;

import java.util.Locale;

import me.suwash.util.i18n.MessageSource;

/**
 * DDDフレームワークの例外クラス。
 */
public class UtilException extends LayerException {

    private static final long serialVersionUID = 1L;

    public UtilException(String messageId) {
        super(messageId);
    }

    public UtilException(String messageId, Locale locale) {
        super(messageId, locale);
    }

    public UtilException(String messageId, Object[] messageArgs) {
        super(messageId, messageArgs);
    }

    public UtilException(String messageId, Object[] messageArgs, Locale locale) {
        super(messageId, messageArgs, locale);
    }

    public UtilException(String messageId, Throwable cause) {
        super(messageId, cause);
    }

    public UtilException(String messageId, Locale locale, Throwable cause) {
        super(messageId, locale, cause);
    }

    public UtilException(String messageId, Object[] messageArgs, Throwable cause) {
        super(messageId, messageArgs, cause);
    }

    public UtilException(String messageId, Object[] messageArgs, Locale locale, Throwable cause) {
        super(messageId, messageArgs, locale, cause);
    }

    @Override
    protected MessageSource getMessageSource() {
        return MessageSource.getInstance();
    }
}

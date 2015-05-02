package me.suwash.util.exception;

import java.util.Locale;

import me.suwash.util.i18n.MessageSource;

import org.apache.commons.lang3.StringUtils;

/**
 * レイヤ毎の例外基底クラス。
 * MessageSource、DdSourceを利用して、他言語対応したメッセージを利用できます。
 */
public abstract class LayerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    protected String messageId;
    protected Object[] messageArgs;
    protected Locale locale;
    protected MessageSource messageSource;

    protected LayerException() {
        super();
        this.messageId = StringUtils.EMPTY;
        this.messageArgs = null;
        this.locale = null;
        this.messageSource = getMessageSource();
    }

    protected LayerException(String messageId) {
        super();
        this.messageId = messageId;
        this.messageArgs = null;
        this.locale = null;
        this.messageSource = getMessageSource();
    }

    protected LayerException(String messageId, Locale locale) {
        super();
        this.messageId = messageId;
        this.messageArgs = null;
        this.locale = locale;
        this.messageSource = getMessageSource();
    }

    protected LayerException(String messageId, Object[] messageArgs) {
        super();
        this.messageId = messageId;
        this.messageArgs = messageArgs;
        this.locale = null;
        this.messageSource = getMessageSource();
    }

    protected LayerException(String messageId, Object[] messageArgs, Locale locale) {
        super();
        this.messageId = messageId;
        this.messageArgs = messageArgs;
        this.locale = locale;
        this.messageSource = getMessageSource();
    }

    protected LayerException(Throwable cause) {
        super(cause);
        this.messageId = StringUtils.EMPTY;
        this.messageArgs = null;
        this.locale = null;
        this.messageSource = getMessageSource();
    }

    protected LayerException(String messageId, Throwable cause) {
        super(cause);
        this.messageId = messageId;
        this.messageArgs = null;
        this.locale = null;
        this.messageSource = getMessageSource();
    }

    protected LayerException(String messageId, Locale locale, Throwable cause) {
        super(cause);
        this.messageId = messageId;
        this.messageArgs = null;
        this.locale = locale;
        this.messageSource = getMessageSource();
    }

    protected LayerException(String messageId, Object[] messageArgs, Throwable cause) {
        super(cause);
        this.messageId = messageId;
        this.messageArgs = messageArgs;
        this.locale = null;
        this.messageSource = getMessageSource();
    }

    protected LayerException(String messageId, Object[] messageArgs, Locale locale, Throwable cause) {
        super(cause);
        this.messageId = messageId;
        this.messageArgs = messageArgs;
        this.locale = locale;
        this.messageSource = getMessageSource();
    }

    /**
     * 対象レイヤのMessageSourceを返すようにオーバーライドしてください。
     */
    abstract protected MessageSource getMessageSource();

    /**
     * ロケールを設定します。
     * @param locale ロケール
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    public Locale getLocale() {
        return this.locale;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public Object[] getMessageArgs() {
        return this.messageArgs;
    }

    @Override
    public String getMessage() {
        if (messageSource == null) {
            throw new RuntimeException("getMessageSourceメソッドを、対象レイヤのMessageSourceを返すようにオーバーライドしてください。");
        }
        return "[" + messageId + "]" + messageSource.getMessage(messageId, messageArgs, locale);
    }
}

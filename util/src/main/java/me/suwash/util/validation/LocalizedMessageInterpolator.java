package me.suwash.util.validation;

import java.util.Locale;

import javax.validation.MessageInterpolator;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;

/**
 * javax.validation向けの、Localeを考慮したメッセージ取得クラス。
 */
public class LocalizedMessageInterpolator implements MessageInterpolator {

    private transient Locale locale;
    private transient MessageInterpolator mi;

    /**
     * コンストラクタ。
     */
    public LocalizedMessageInterpolator() {
        this.locale = Locale.getDefault();
        this.mi = new ResourceBundleMessageInterpolator();
    }

    /**
     * コンストラクタ。
     *
     * @param locale ロケール
     */
    public LocalizedMessageInterpolator(final Locale locale) {
        this();
        this.locale = locale;
    }

    /**
     * ロケールを設定します。
     *
     * @param locale ロケール
     */
    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    @Override
    public String interpolate(final String messageTemplate, final Context context) {
        return mi.interpolate(messageTemplate, context, this.locale);
    }

    @Override
    public String interpolate(final String messageTemplate, final Context context, final Locale locale) {
        return mi.interpolate(messageTemplate, context, locale);
    }

}

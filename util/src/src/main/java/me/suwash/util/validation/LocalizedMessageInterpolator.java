package me.suwash.util.validation;

import java.util.Locale;

import javax.validation.MessageInterpolator;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;

public class LocalizedMessageInterpolator implements MessageInterpolator {

    private Locale locale;
    private MessageInterpolator mi;

    public LocalizedMessageInterpolator() {
        this.locale = Locale.getDefault();
        this.mi = new ResourceBundleMessageInterpolator();
    }

    public LocalizedMessageInterpolator(Locale locale) {
        this();
        this.locale = locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String interpolate(String messageTemplate, Context context) {
        return mi.interpolate(messageTemplate, context, this.locale);
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        return mi.interpolate(messageTemplate, context, locale);
    }
}

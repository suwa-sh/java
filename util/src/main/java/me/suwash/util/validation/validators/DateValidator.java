package me.suwash.util.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import me.suwash.util.DateUtils;
import me.suwash.util.validation.constraints.Date;

/**
 * BeanValidation 日付フォーマットチェック。
 */
public class DateValidator implements ConstraintValidator<Date, String> {

    @Override
    public void initialize(final Date constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String target, final ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(target)) {
            // 未設定は NotEmpty に移譲。
            return true;
        }

        try {
            DateUtils.toDate(target);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}

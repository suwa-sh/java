package me.suwash.util.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import me.suwash.util.validation.validators.DateValidator;

/**
 * BeanValidation 日時フォーマットチェックアノテーション。
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
public @interface Date {
    /** メッセージID。 */
    String message() default "{me.suwash.util.validation.constraints.Date}";

    /** グループ。 */
    Class<?>[] groups() default {};

    /** メタ情報。 */
    Class<? extends Payload>[] payload() default {};
}

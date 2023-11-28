package com.discordshopping.validation.annotation;

import com.discordshopping.validation.constraint.ValidEmailConstraint;
import com.discordshopping.exception.enums.ErrorMessage;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Constraint(validatedBy = ValidEmailConstraint.class)
public @interface ValidEmail {
    String message() default ErrorMessage.INVALID_EMAIL_FORMAT;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

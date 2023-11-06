package com.discordshopping.bot.util.validator.annotation;

import com.discordshopping.bot.util.validator.constraint.ValidUUIDConstraint;
import com.discordshopping.exception.enums.ErrorMessage;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Constraint(validatedBy = ValidUUIDConstraint.class)
public @interface ValidUUID {
    String message() default ErrorMessage.INVALID_UUID_FORMAT;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


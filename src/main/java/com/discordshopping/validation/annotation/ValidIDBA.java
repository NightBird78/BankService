package com.discordshopping.validation.annotation;

import com.discordshopping.validation.constraint.ValidIDBAConstraint;
import com.discordshopping.exception.enums.ErrorMessage;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Constraint(validatedBy = ValidIDBAConstraint.class)
public @interface ValidIDBA {
    String message() default ErrorMessage.INVALID_IDBA;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

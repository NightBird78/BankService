package com.discordshopping.validation.annotation;

import com.discordshopping.validation.constraint.NullFieldConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Constraint(validatedBy = NullFieldConstraint.class)
public @interface NullField {
    String message() default "one of field is NULL";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package com.discordshopping.validation.annotation;

import com.discordshopping.validation.constraint.NumLikeConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Constraint(validatedBy = NumLikeConstraint.class)
public @interface NumLike {
    String message() default "seems unlike number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

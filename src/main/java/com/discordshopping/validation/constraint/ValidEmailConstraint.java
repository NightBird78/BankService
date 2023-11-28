package com.discordshopping.validation.constraint;

import com.discordshopping.validation.annotation.ValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class ValidEmailConstraint implements ConstraintValidator<ValidEmail, String> {
    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String t, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(t)
                .filter(s -> !s.isBlank())
                .map(s -> s.matches("^[a-z0-9]+@[a-z]+.[a-z]+$"))
                .orElse(false);
    }
}

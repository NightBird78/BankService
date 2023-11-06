package com.discordshopping.bot.util.validator.constraint;

import com.discordshopping.bot.util.validator.annotation.ValidEmail;
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
        System.out.println("*** " + t);
        return Optional.ofNullable(t)
                .filter(s -> !s.isBlank())
                .map(s -> s.matches("^[a-z0-9]+@[a-z]+.[a-z]+$"))
                .orElse(false);
    }
}

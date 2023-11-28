package com.discordshopping.validation.constraint;

import com.discordshopping.validation.annotation.ValidIDBA;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class ValidIDBAConstraint implements ConstraintValidator<ValidIDBA, String> {
    @Override
    public void initialize(ValidIDBA constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(string)
                .filter(s -> !s.isBlank())
                .map(s -> s.matches("^IDBA[0-9]{16}$"))
                .orElse(false);
    }
}

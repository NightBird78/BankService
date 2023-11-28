package com.discordshopping.validation.constraint;

import com.discordshopping.validation.annotation.ValidUUID;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class ValidUUIDConstraint implements ConstraintValidator<ValidUUID, String> {
    @Override
    public void initialize(ValidUUID constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String t, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(t)
                .filter(s -> !s.isBlank())
                .map(s -> s.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"))
                .orElse(false);
    }
}

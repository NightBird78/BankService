package com.discordshopping.validation.constraint;

import com.discordshopping.validation.annotation.NumLike;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NumLikeConstraint implements ConstraintValidator<NumLike, String> {

    @Override
    public void initialize(NumLike constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException ignore) {
            return false;
        }

        return true;
    }
}

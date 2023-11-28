package com.discordshopping.validation.constraint;

import com.discordshopping.validation.annotation.NullField;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class NullFieldConstraint implements ConstraintValidator<NullField, Object> {

    @Override
    public void initialize(NullField constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        Class<?> clazz = object.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object value;

            try {
                value = field.get(object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            if (value == null) {
                return false;
            }
        }
        return true;
    }
}
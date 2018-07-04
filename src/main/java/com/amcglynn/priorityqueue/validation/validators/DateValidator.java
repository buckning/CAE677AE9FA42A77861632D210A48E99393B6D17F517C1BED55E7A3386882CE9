package com.amcglynn.priorityqueue.validation.validators;

import com.amcglynn.priorityqueue.validation.annotations.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<Date, String> {

    private static final String VALID_DATE_REGEX = "([0-9]){8}";

    @Override
    public void initialize(Date uri) {
    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext constraintContext) {
        if (input == null) {
            return false;
        }

        return input.matches(VALID_DATE_REGEX);
    }
}

package com.amcglynn.priorityqueue.validation.validators;

import com.amcglynn.priorityqueue.validation.annotations.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateValidator implements ConstraintValidator<Date, String> {

    @Override
    public void initialize(Date uri) {
    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext constraintContext) {
        if (input == null) {
            return false;
        }

        try {
            new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").parse(input);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}

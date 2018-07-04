package com.amcglynn.priorityqueue.validation.annotations;

import com.amcglynn.priorityqueue.validation.validators.DateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DateValidator.class)
public @interface Date {
    String message() default "Must be a Date in the format of DDMMYYYY";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

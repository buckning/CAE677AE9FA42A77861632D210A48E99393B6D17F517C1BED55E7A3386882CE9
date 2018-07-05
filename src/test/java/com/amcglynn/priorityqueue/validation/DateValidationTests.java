package com.amcglynn.priorityqueue.validation;

import com.amcglynn.priorityqueue.requests.WorkOrderRequest;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(DataProviderRunner.class)
public class DateValidationTests {

    private ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private Validator validator = validatorFactory.getValidator();

    @Test
    public void testValidDateFormatPassesValidation() {
        WorkOrderRequest workOrderRequest = new WorkOrderRequest(1L, "2018-01-01-00-00-00");
        Set<ConstraintViolation<WorkOrderRequest>> violations = validator.validate(workOrderRequest);
        assertThat(violations.isEmpty()).isTrue();
    }

    @UseDataProvider("invalidDateProvider")
    @Test
    public void testInvalidDateFailsValidation(String invalidDate) {
        WorkOrderRequest workOrderRequest = new WorkOrderRequest(1L, invalidDate);
        Set<ConstraintViolation<WorkOrderRequest>> violations = validator.validate(workOrderRequest);
        assertThat(violations.isEmpty()).isFalse();
    }

    @DataProvider
    public static Object[][] invalidDateProvider() {
        return new Object[][] {
                {null},
                {"abc"},
                {"0011333"},
                {""},
                {"0"},
                {"010120181"},
                {"0101a018"}
        };
    }
}

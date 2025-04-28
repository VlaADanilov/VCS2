package com.technokratos.vcs2.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ValidYearValidator implements ConstraintValidator<ValidYear, Integer> {
    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext constraintValidatorContext) {
        if (year == null) {
            return true;
        }

        int currentYear = LocalDate.now().getYear();
        return year <= currentYear;
    }
}

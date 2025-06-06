package com.technokratos.vcs2.util;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = ValidYearValidator.class)
@Documented
public @interface ValidYear {
    String message() default "Год, который вы ввели, превышает нынешний год";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}

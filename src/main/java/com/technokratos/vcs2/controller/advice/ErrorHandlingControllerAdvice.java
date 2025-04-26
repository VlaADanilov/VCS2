package com.technokratos.vcs2.controller.advice;

import com.technokratos.vcs2.model.dto.response.ValidationErrorResponse;
import com.technokratos.vcs2.model.dto.response.Violation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.List;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(
            ConstraintViolationException ex
    ) {
        final List<Violation> violations = ex.getConstraintViolations().stream()
                .map(
                        violation -> {
                            Violation v = new Violation();
                            v.setMessage(violation.getMessage());
                            String[] s = violation.getPropertyPath().toString().split("\\.");
                            v.setFieldName(s[s.length - 1]);
                            return v;
                        }
                ).toList();
        return new ValidationErrorResponse(violations);
    }
}

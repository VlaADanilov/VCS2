package com.technokratos.vcs2.model.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ValidationErrorResponse {
    private List<Violation> violations;

    public ValidationErrorResponse(List<Violation> violations) {
        this.violations = violations;
    }
}

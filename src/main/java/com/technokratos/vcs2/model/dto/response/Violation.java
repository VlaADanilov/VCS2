package com.technokratos.vcs2.model.dto.response;

import lombok.Data;

@Data
public class Violation {
    private String fieldName;
    private String message;
}

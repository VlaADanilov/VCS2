package com.technokratos.vcs2.model.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReportRequestDto {
    @NotBlank
    private String comment;
}

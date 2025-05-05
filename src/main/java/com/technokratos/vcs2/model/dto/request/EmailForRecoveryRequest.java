package com.technokratos.vcs2.model.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class EmailForRecoveryRequest {
    @Email
    @NotBlank
    private String email;
}

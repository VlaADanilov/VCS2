package com.technokratos.vcs2.model.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data

public class EmailWithCodeAndNewPasswordDto {
    @Email
    @NotBlank
    private String email;

    @Size(min = 3)
    private String password;

    @NotBlank
    private String code;
}

package com.technokratos.vcs2.model.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegisterUserDto {

    @Size(min = 2, max = 20)
    @NotBlank
    private String username;

    @Size(min = 2, max = 20)
    @NotBlank
    private String password;

    @Email
    @NotBlank
    private String email;
}

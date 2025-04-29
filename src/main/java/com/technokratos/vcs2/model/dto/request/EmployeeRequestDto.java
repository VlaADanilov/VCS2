package com.technokratos.vcs2.model.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class EmployeeRequestDto {

    @NotBlank
    @Size(min = 4)
    private String name;
    @NotBlank
    @Size(min = 4)
    private String profession;

    private String description;

    @Pattern(regexp = "^(\\+7|8)+([0-9]){10}$",
    message = "Номер не соответсвует паттерну: +7########## или 8##########")
    private String phone;

    @NotBlank
    @Size(min = 2, max = 20)
    private String accountName;
}

package com.technokratos.vcs2.model.dto.request;

import com.technokratos.vcs2.util.ValidYear;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@NoArgsConstructor
public class AutoRequestDto {
    @Size(min = 3, max = 20)
    @NotBlank
    private String model;

    @Min(1950)
    @ValidYear
    private Integer year;

    @Min(0)
    private Integer price;

    @Min(0)
    private Integer mileage;

    @Size(min = 3, max = 20)
    @NotBlank
    private String city;

    @Pattern(regexp = "^\\+7[0-9]{10}$", message = "Неверный формат номера телефона")
    private String phone;

    private String description;

    private UUID brand_id;

}

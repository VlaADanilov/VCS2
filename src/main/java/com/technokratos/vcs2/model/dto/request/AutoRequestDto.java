package com.technokratos.vcs2.model.dto.request;

import java.util.UUID;

public record AutoRequestDto(String model,
                             Integer year,
                             Integer price,
                             Integer mileage,
                             String city,
                             String description,
                             UUID brand_id) {
}

package com.technokratos.vcs2.model.dto.response;

public record AutoResponseDto(String model,
                              Integer year,
                              Integer price,
                              Integer mileage,
                              String city,
                              String description,
                              String phone) {
}

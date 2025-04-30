package com.technokratos.vcs2.model.dto.response;

import java.util.List;
import java.util.UUID;

public record AutoResponseDto(String model,
                              Integer year,
                              Integer price,
                              Integer mileage,
                              String city,
                              String description,
                              String phone,
                              UUID brandId,
                              List<UUID> images) {
}

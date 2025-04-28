package com.technokratos.vcs2.model.dto.response;

import java.util.UUID;

public record BrandResponseDto(
        UUID id,
        String name,
        String country
) {
}

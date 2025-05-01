package com.technokratos.vcs2.model.dto.response;

import java.util.UUID;

public record EmployeeResponseDto(
        UUID id,
        String name,
        String profession,
        String description,
        String username,
        String phone,
        UUID image
) {
}

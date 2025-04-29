package com.technokratos.vcs2.model.dto.response;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String username,
        String role) {
}

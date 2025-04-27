package com.technokratos.vcs2.model.dto.response;

import java.util.UUID;

public record ListElementAutoResponseDto(UUID id,
                                         String username,
                                         AutoResponseDto autoInfo,
                                         String brandName){
}

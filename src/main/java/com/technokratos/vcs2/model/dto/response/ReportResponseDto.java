package com.technokratos.vcs2.model.dto.response;

import java.util.List;
import java.util.UUID;

public record ReportResponseDto(
        UUID uuid,
        String comment,
        String reporter,
        UUID autoId,
        List<String> viewedNames
) {
}

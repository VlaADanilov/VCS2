package com.technokratos.vcs2.exception.notFound;

import java.util.UUID;

public class ReportNotFoundException extends NotFoundException {
    public ReportNotFoundException(UUID reportId) {
        super("Report with id %s not found".formatted(reportId), "/");
    }
}

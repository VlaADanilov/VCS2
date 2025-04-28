package com.technokratos.vcs2.exception.notFound;

import java.util.UUID;

public class AutoNotFoundException extends NotFoundException {
    public AutoNotFoundException(UUID id) {
        super("Auto with id %s not found".formatted(id), "/auto");
    }
}

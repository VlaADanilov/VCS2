package com.technokratos.vcs2.exception.notFound;

import java.util.UUID;

public class BrandNotFoundException extends NotFoundException {
    public BrandNotFoundException(UUID brandId) {
        super("Brand with id %s not found".formatted(brandId), "/");
    }
}

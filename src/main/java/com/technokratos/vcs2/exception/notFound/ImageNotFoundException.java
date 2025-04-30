package com.technokratos.vcs2.exception.notFound;

import java.util.UUID;

public class ImageNotFoundException extends NotFoundException {
    public ImageNotFoundException(UUID imageId) {
        super("image with id %s not found".formatted(imageId), "/");
    }
}

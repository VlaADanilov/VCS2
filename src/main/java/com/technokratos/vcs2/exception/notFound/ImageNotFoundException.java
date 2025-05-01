package com.technokratos.vcs2.exception.notFound;

import java.util.UUID;

public class ImageNotFoundException extends NotFoundException {
    public ImageNotFoundException(UUID imageId) {
        super("image with id %s not found".formatted(imageId), "/");
    }

    public ImageNotFoundException(UUID imageId, UUID autoId, String str) {
        super("image with id %s not found in this %s :%s".formatted(imageId,str, autoId), "/");
    }
}

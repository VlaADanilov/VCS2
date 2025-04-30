package com.technokratos.vcs2.service;

import com.technokratos.vcs2.model.entity.Image;

import java.util.UUID;

public interface ImageService {
    UUID saveImage(String name);

    Image getImageById(UUID imageId);

    void checkForExistsImageAndAutoContainsIt(UUID autoId, UUID imageId);

    void deleteImage(UUID imageId, UUID autoId);
}

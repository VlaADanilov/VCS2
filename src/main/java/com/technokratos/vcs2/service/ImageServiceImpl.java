package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.notFound.ImageNotFoundException;
import com.technokratos.vcs2.model.entity.Image;
import com.technokratos.vcs2.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    @Override
    public UUID saveImage(String name) {
        Image image = new Image();
        image.setImage(name);
        image.setId(UUID.randomUUID());
        imageRepository.save(image);
        return image.getId();
    }

    @Override
    public Image getImageById(UUID imageId) {
        return imageRepository.findById(imageId).orElseThrow(() -> new ImageNotFoundException(imageId));
    }
}

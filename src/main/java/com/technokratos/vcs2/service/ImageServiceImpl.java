package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.notFound.AutoNotFoundException;
import com.technokratos.vcs2.exception.notFound.ImageNotFoundException;
import com.technokratos.vcs2.model.entity.Auto;
import com.technokratos.vcs2.model.entity.Image;
import com.technokratos.vcs2.repository.AutoRepository;
import com.technokratos.vcs2.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final AutoRepository autoRepository;

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

    @Override
    public void checkForExistsImageAndAutoContainsIt(UUID autoId, UUID imageId) {
        if (!imageRepository.existsById(imageId)) {
            throw new ImageNotFoundException(imageId);
        }
        Optional<Image> imageWhereAutoId = autoRepository.getImageWhereAutoId(autoId, imageId);
        if (imageWhereAutoId.isEmpty()) {
            throw new ImageNotFoundException(imageId, autoId);
        }
    }

    @Override
    public void deleteImage(UUID imageId, UUID autoId) {
        Auto auto = autoRepository.findById(autoId).orElseThrow(() -> new AutoNotFoundException(autoId));
        for (Image image : auto.getImages()) {
            if (image.getId().equals(imageId)) {
                auto.getImages().remove(image);
                break;
            }
        }
        autoRepository.save(auto);
        imageRepository.deleteById(imageId);
    }
}

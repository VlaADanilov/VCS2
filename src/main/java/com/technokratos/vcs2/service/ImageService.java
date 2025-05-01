package com.technokratos.vcs2.service;

import com.technokratos.vcs2.model.entity.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ImageService {
    UUID saveImage(String name);

    Image getImageById(UUID imageId);

    void checkForExistsImageAndAutoContainsIt(UUID autoId, UUID imageId);

    void deleteImage(UUID imageId, UUID autoId);

    void checkForExistsImageAndEmployeeContainsIt(UUID employeeId, UUID imageId);

    void deleteImageFromEmployee(UUID imageId, UUID employeeId);

    ResponseEntity<String> uploadImage(MultipartFile file);

    void deleteFromComputer(String filename);
}

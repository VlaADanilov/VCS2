package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.api.ImageApi;
import com.technokratos.vcs2.model.entity.Image;
import com.technokratos.vcs2.service.AutoService;
import com.technokratos.vcs2.service.EmployeeService;
import com.technokratos.vcs2.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ImageController implements ImageApi {
    private final AutoService autoService;
    private final ImageService imageService;
    private final EmployeeService employeeService;

    private final Path rootLocation = Paths.get("/vcs");

    public ResponseEntity<String> upload(MultipartFile file,
                                         UUID auto_id) {
        autoService.checkForExistsAuto(auto_id);
        ResponseEntity<String> response = imageService.uploadImage(file);
        if (response.getStatusCode().is2xxSuccessful()) {
            UUID image_id = imageService.saveImage(response.getBody());
            autoService.addImageToAuto(auto_id, image_id);
            return ResponseEntity.ok(image_id.toString());
        } else {
            return response;
        }
    }

    public ResponseEntity<String> deleteImageFromAuto(UUID auto_id,
                                                      UUID image_id) {
        autoService.checkForExistsAuto(auto_id);
        imageService.checkForExistsImageAndAutoContainsIt(auto_id, image_id);
        imageService.deleteImage(image_id, auto_id);

        return ResponseEntity.ok().build();
    }


    public ResponseEntity<Resource> viewImage(UUID image_id) {
        Image image = imageService.getImageById(image_id);
        String filename = image.getImage();
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = determineContentType(filename);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<String> deleteEmployeeImage(UUID employeeId,
                                                      UUID imageId) {
        employeeService.checkForExistsEmployee(employeeId);
        imageService.checkForExistsImageAndEmployeeContainsIt(employeeId,imageId);
        imageService.deleteImageFromEmployee(imageId, employeeId);

        return ResponseEntity.ok().build();
    }


    public ResponseEntity<String> uploadImageToEmployee(MultipartFile file,
                                                        UUID employeeId) {
        employeeService.checkForExistsEmployee(employeeId);
        ResponseEntity<String> response = imageService.uploadImage(file);
        if (response.getStatusCode().is2xxSuccessful()) {
            UUID image_id = imageService.saveImage(response.getBody());
            employeeService.addImageToEmployee(employeeId, image_id);
            return ResponseEntity.ok(image_id.toString());
        } else {
            return response;
        }
    }

    private String determineContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        return switch (extension) {
            case ".jpg", ".jpeg" -> "image/jpeg";
            case ".png" -> "image/png";
            case ".gif" -> "image/gif";
            case ".bmp" -> "image/bmp";
            case ".webp" -> "image/webp";
            default -> "application/octet-stream";
        };
    }
}

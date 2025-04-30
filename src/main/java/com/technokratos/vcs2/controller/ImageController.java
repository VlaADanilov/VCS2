package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.model.entity.Image;
import com.technokratos.vcs2.service.AutoService;
import com.technokratos.vcs2.service.ImageService;
import com.technokratos.vcs2.util.FileSystemProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {
    private final AutoService autoService;
    private final ImageService imageService;
    private Path rootLocation = Paths.get("/vcs");

    // Список разрешенных расширений
    private final List<String> allowedExtensions = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"
    );

    @PostMapping("/upload/auto/{auto_id}")
    @PreAuthorize("@securityService.canDelete(#auto_id, authentication.name) " +
            "or hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file
    , @PathVariable("auto_id") UUID auto_id) {
        autoService.checkForExistsAuto(auto_id);
        ResponseEntity<String> response = uploadImage(file);
        if (response.getStatusCode().is2xxSuccessful()) {
            UUID image_id = imageService.saveImage(response.getBody());
            autoService.addImageToAuto(auto_id, image_id);
            return ResponseEntity.ok(image_id.toString());
        } else {
            return response;
        }
    }


    private ResponseEntity<String> uploadImage(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Файл пуст");
            }

            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase() : "";

            if (!allowedExtensions.contains(fileExtension)) {
                return ResponseEntity.badRequest().body(
                        "Недопустимый тип файла. Разрешены: " + allowedExtensions
                );
            }

            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }

            UUID uuid = UUID.randomUUID();
            String newFilename = uuid + fileExtension;

            Files.copy(
                    file.getInputStream(),
                    rootLocation.resolve(newFilename),
                    StandardCopyOption.REPLACE_EXISTING
            );

            return ResponseEntity.ok(newFilename);

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Ошибка при загрузке файла: " + e.getMessage());
        }
    }

    @GetMapping("/{image_id}")
    public ResponseEntity<Resource> viewImage(@PathVariable("image_id") UUID image_id) {
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

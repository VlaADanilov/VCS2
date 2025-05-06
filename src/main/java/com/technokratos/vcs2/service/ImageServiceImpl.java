package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.notFound.AutoNotFoundException;
import com.technokratos.vcs2.exception.notFound.EmployeeNotFoundException;
import com.technokratos.vcs2.exception.notFound.ImageNotFoundException;
import com.technokratos.vcs2.model.entity.Auto;
import com.technokratos.vcs2.model.entity.Employee;
import com.technokratos.vcs2.model.entity.Image;
import com.technokratos.vcs2.repository.AutoRepository;
import com.technokratos.vcs2.repository.EmployeeRepository;
import com.technokratos.vcs2.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final AutoRepository autoRepository;
    private final EmployeeRepository employeeRepository;

    // Список разрешенных расширений
    private final List<String> allowedExtensions = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"
    );

    private Path rootLocation = Paths.get("/vcs");

    @Override
    public UUID saveImage(String name) {
        log.info("Saving new image with name: {}", name);
        Image image = new Image();
        image.setImage(name);
        image.setId(UUID.randomUUID());
        imageRepository.save(image);
        log.info("Image saved successfully with ID: {}", image.getId());
        return image.getId();
    }

    @Override
    public Image getImageById(UUID imageId) {
        log.debug("Fetching image by ID: {}", imageId);
        return imageRepository.findById(imageId)
                .orElseThrow(() -> {
                    log.warn("Image with ID {} not found", imageId);
                    return new ImageNotFoundException(imageId);
                });
    }

    @Override
    public void checkForExistsImageAndAutoContainsIt(UUID autoId, UUID imageId) {
        log.debug("Checking if image {} exists and is associated with auto {}", imageId, autoId);

        if (!imageRepository.existsById(imageId)) {
            log.warn("Image with ID {} does not exist", imageId);
            throw new ImageNotFoundException(imageId);
        }

        Optional<Image> imageWhereAutoId = autoRepository.getImageWhereAutoId(autoId, imageId);
        if (imageWhereAutoId.isEmpty()) {
            log.warn("Image {} is not associated with auto {}", imageId, autoId);
            throw new ImageNotFoundException(imageId, autoId, "auto");
        }
    }

    @Override
    public void deleteImage(UUID imageId, UUID autoId) {
        log.info("Deleting image {} from auto {}", imageId, autoId);

        Auto auto = autoRepository.findById(autoId)
                .orElseThrow(() -> {
                    log.warn("Auto with ID {} not found", autoId);
                    return new AutoNotFoundException(autoId);
                });

        for (Image image : auto.getImages()) {
            if (image.getId().equals(imageId)) {
                log.info("Removing image {} from auto {}", imageId, autoId);
                auto.getImages().remove(image);
                deleteFromComputer(image.getImage());
                break;
            }
        }

        autoRepository.save(auto);
        imageRepository.deleteById(imageId);
        log.info("Image {} deleted successfully from auto {}", imageId, autoId);
    }

    @Override
    public void checkForExistsImageAndEmployeeContainsIt(UUID employeeId, UUID imageId) {
        log.debug("Checking if image {} exists and is associated with employee {}", imageId, employeeId);

        if (!imageRepository.existsById(imageId)) {
            log.warn("Image with ID {} does not exist", imageId);
            throw new ImageNotFoundException(imageId);
        }

        Optional<Image> imageWhereEmployeeId = employeeRepository.getImageWhereEmployeeId(employeeId, imageId);
        if (imageWhereEmployeeId.isEmpty()) {
            log.warn("Image {} is not associated with employee {}", imageId, employeeId);
            throw new ImageNotFoundException(imageId, employeeId, "employee");
        }
    }

    @Override
    public void deleteImageFromEmployee(UUID imageId, UUID employeeId) {
        log.info("Deleting image {} from employee {}", imageId, employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    log.warn("Employee with ID {} not found", employeeId);
                    return new EmployeeNotFoundException(employeeId);
                });

        if (employee.getImage() != null && employee.getImage().getId().equals(imageId)) {
            log.info("Removing image {} from employee {}", imageId, employeeId);
            deleteFromComputer(employee.getImage().getImage());
            employee.setImage(null);
        }

        employeeRepository.save(employee);
        imageRepository.deleteById(imageId);
        log.info("Image {} deleted successfully from employee {}", imageId, employeeId);
    }

    public ResponseEntity<String> uploadImage(MultipartFile file) {
        log.info("Uploading new image file: {}", file.getOriginalFilename());

        try {
            if (file.isEmpty()) {
                log.warn("Attempt to upload empty file");
                return ResponseEntity.badRequest().body("Файл пуст");
            }

            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase() : "";

            if (!allowedExtensions.contains(fileExtension)) {
                log.warn("Invalid file extension: {}", fileExtension);
                return ResponseEntity.badRequest().body(
                        "Недопустимый тип файла. Разрешены: " + allowedExtensions
                );
            }

            if (!Files.exists(rootLocation)) {
                log.info("Creating directory for images: {}", rootLocation);
                Files.createDirectories(rootLocation);
            }

            UUID uuid = UUID.randomUUID();
            String newFilename = uuid + fileExtension;

            Files.copy(
                    file.getInputStream(),
                    rootLocation.resolve(newFilename),
                    StandardCopyOption.REPLACE_EXISTING
            );

            log.info("File uploaded successfully as: {}", newFilename);
            return ResponseEntity.ok(newFilename);

        } catch (IOException e) {
            log.error("Error while uploading file", e);
            return ResponseEntity.internalServerError()
                    .body("Ошибка при загрузке файла: " + e.getMessage());
        }
    }

    @Override
    public void deleteFromComputer(String filename) {
        log.info("Deleting image file from disk: {}", filename);

        if (filename == null || filename.isEmpty()) {
            log.warn("Attempt to delete file with empty or null name");
            throw new IllegalArgumentException("Имя файла не может быть пустым");
        }

        try {
            Path filePath = rootLocation.resolve(filename).normalize();

            if (Files.exists(filePath) && filePath.startsWith(rootLocation.normalize())) {
                Files.delete(filePath);
                log.info("File {} deleted successfully", filename);
            } else {
                log.warn("File {} not found or path is invalid", filename);
                throw new FileNotFoundException("Файл не найден или путь выходит за пределы разрешенной директории");
            }
        } catch (IOException e) {
            log.error("Error while deleting file: {}", filename, e);
            throw new RuntimeException("Ошибка при удалении файла: " + filename, e);
        }
    }
}

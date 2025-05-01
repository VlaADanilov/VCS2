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
            throw new ImageNotFoundException(imageId, autoId, "auto");
        }
    }

    @Override
    public void deleteImage(UUID imageId, UUID autoId) {
        Auto auto = autoRepository.findById(autoId).orElseThrow(() -> new AutoNotFoundException(autoId));
        for (Image image : auto.getImages()) {
            if (image.getId().equals(imageId)) {
                auto.getImages().remove(image);
                deleteFromComputer(image.getImage());
                break;
            }
        }
        autoRepository.save(auto);
        imageRepository.deleteById(imageId);
    }

    @Override
    public void checkForExistsImageAndEmployeeContainsIt(UUID employeeId, UUID imageId) {
        if (!imageRepository.existsById(imageId)) {
            throw new ImageNotFoundException(imageId);
        }
        Optional<Image> imageWhereEmployeeId = employeeRepository.getImageWhereEmployeeId(employeeId, imageId);
        if (imageWhereEmployeeId.isEmpty()) {
            throw new ImageNotFoundException(imageId, employeeId, "employee");
        }
    }

    @Override
    public void deleteImageFromEmployee(UUID imageId, UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        if (employee.getImage() != null && employee.getImage().getId().equals(imageId)) {
            deleteFromComputer(employee.getImage().getImage());
            employee.setImage(null);
        }
        employeeRepository.save(employee);
        imageRepository.deleteById(imageId);
    }

    public ResponseEntity<String> uploadImage(MultipartFile file) {
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

    @Override
    public void deleteFromComputer(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Имя файла не может быть пустым");
        }

        try {
            Path filePath = rootLocation.resolve(filename).normalize();

            if (Files.exists(filePath) && filePath.startsWith(rootLocation.normalize())) {
                Files.delete(filePath);
            } else {
                throw new FileNotFoundException("Файл не найден или путь выходит за пределы разрешенной директории");
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при удалении файла: " + filename, e);
        }
    }
}

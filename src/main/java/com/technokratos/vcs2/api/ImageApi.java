package com.technokratos.vcs2.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequestMapping("/api/image")
@Tag(name = "ImageController",
    description = "Контроллер для работы с изображениями")
public interface ImageApi {

    @PostMapping("/upload/auto/{auto_id}")
    @PreAuthorize("@securityService.canDelete(#auto_id, authentication.name) " +
            "or hasAnyRole('ADMIN', 'MODERATOR')")
    @SecurityRequirement(name = "baseAuth")
    @Operation(summary = "Загрузить изображение к объявлению",
            description = """
    Загрузить изображение к объявлению. Это должно быть ваше объявление или вы должны иметь
     хотя бы права модератора""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "404", description = "Нет такого объявления"),
            @ApiResponse(responseCode = "403", description = "У вас нет прав"),
            @ApiResponse(responseCode = "400", description = "Читай body")
    })
    ResponseEntity<String> upload(@RequestParam("file")
                                  @Parameter(description = "файл", required = true)
                                  MultipartFile file,
                                  @PathVariable("auto_id")
                                  @Parameter(description = "id объявления", required = true)
                                  UUID auto_id);

    @DeleteMapping("/auto/{auto_id}/delete/{image_id}")
    @PreAuthorize("@securityService.canDelete(#auto_id, authentication.name) " +
            "or hasAnyRole('ADMIN', 'MODERATOR')")
    @SecurityRequirement(name = "baseAuth")
    @Operation(summary = "Удалить изображение с объявления",
            description = """
    Удалить изображение с объявления. Это должно быть ваше объявление или вы должны иметь
     хотя бы права модератора""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "404", description = "Нет такого объявления"),
            @ApiResponse(responseCode = "403", description = "У вас нет прав"),
            @ApiResponse(responseCode = "400", description = "Неправильный формат UUID")
    })
    ResponseEntity<String> deleteImageFromAuto(@PathVariable("auto_id")
                                               @Parameter(description = "id объявления", required = true)
                                               UUID auto_id,
                                               @PathVariable("image_id")
                                               @Parameter(description = "id изображения", required = true)
                                               UUID image_id);

    @GetMapping("/{image_id}")
    @Operation(summary = "Получить изображение",
            description = "Получить изображение по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "404", description = "Нет такого изображения"),
            @ApiResponse(responseCode = "400", description = "Неправильный формат UUID")
    })
    ResponseEntity<Resource> viewImage(@PathVariable("image_id")
                                       @Parameter(description = "id изображения", required = true)
                                       UUID image_id);

    @DeleteMapping("/employee/{employeeId}/delete/{imageId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "baseAuth")
    @Operation(summary = "Удалить изображение с сотрудника",
            description = "Удалить изображение с сотрудника по id сотрудника и изображения (Нужны права админа!)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "404", description = "Нет такого сотрудника или изображения или они не связаны"),
            @ApiResponse(responseCode = "403", description = "У вас нет прав"),
            @ApiResponse(responseCode = "400", description = "Неправильный формат UUID")
    })
    ResponseEntity<String> deleteEmployeeImage(@PathVariable("employeeId")
                                               @Parameter(description = "id сотрудника", required = true)
                                               UUID employeeId,
                                               @PathVariable("imageId")
                                               @Parameter(description = "id изображения", required = true)
                                               UUID imageId);


    @PostMapping("/upload/employee/{employee_id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "baseAuth")
    @Operation(summary = "Загрузить изображение к сотруднику",
            description = "Загрузить изображение к сотруднику по id сотрудника и файлу(Нужны права админа!)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "404", description = "Нет такого сотрудника"),
            @ApiResponse(responseCode = "403", description = "У вас нет прав"),
            @ApiResponse(responseCode = "400", description = "Читай body")
    })
    ResponseEntity<String> uploadImageToEmployee(@RequestParam("file")
                                                 @Parameter(description = "файл", required = true)
                                                 MultipartFile file,
                                                 @PathVariable("employee_id")
                                                 @Parameter(description = "id сотрудника", required = true)
                                                 UUID employeeId);
}

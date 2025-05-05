package com.technokratos.vcs2.api;

import com.technokratos.vcs2.model.dto.request.AutoRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/auto")
@Tag(name = "AutoController", description = "Возможности, связанные с авто")
public interface AutoApi {
    @GetMapping
    String getAllAutoPageable(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) UUID brand_id);

    @GetMapping("/{car_id}")
    String getAuto(@PathVariable("car_id") UUID carId,
                   Model model,
                   @RequestParam(required = false, defaultValue = "/auto") String referer);

    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    String addAutoForm(Model model);

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @Operation(
            summary = "Добавление объявления",
            description = "Позволяет добавить объявление по продаже авто"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильные данные, читай описание"),
            @ApiResponse(responseCode = "403", description = "Вы не авторизованы")
    })
    @SecurityRequirement(name = "baseAuth")
    UUID addAuto(@RequestBody
                 @Parameter(description = "Данные об автомобиле", required = true)
                 AutoRequestDto auto);

    @DeleteMapping("/{car_id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @SecurityRequirement(name = "baseAuth")
    @PreAuthorize("@securityService.canDelete(#carId, authentication.name) " +
            "or hasAnyRole('ADMIN', 'MODERATOR')")
    @Operation(
            summary = "Удаление объявления",
            description = """
    Позволяет удалить объявление по продаже авто\s
    (Нужны права модератора или быть владельцем объявления!)"""
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильные UUID"),
            @ApiResponse(responseCode = "403", description = "Нет прав"),
            @ApiResponse(responseCode = "404", description = "Не найдено такого объявления")
    })
    void deleteAuto(@PathVariable("car_id")
                    @Parameter(description = "id объявления", required = true)
                    UUID carId);

    @GetMapping("/{car_id}/updateForm")
    @PreAuthorize("@securityService.canDelete(#carId, authentication.name) " +
            "or hasAnyRole('ADMIN', 'MODERATOR')")
    String updateAutoForm(@PathVariable("car_id") UUID carId, Model model);

    @PutMapping("/{car_id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("@securityService.canDelete(#carId, authentication.name) " +
            "or hasAnyRole('ADMIN', 'MODERATOR')")
    @SecurityRequirement(name = "baseAuth")
    @Operation(
            summary = "обновить объявление",
            description = """
    Позволяет обновить объявление по продаже авто\s
    (Нужны права модератора или быть владельцем объявления!)"""
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильные UUID или формат обновлённых данных"),
            @ApiResponse(responseCode = "403", description = "Нет прав"),
            @ApiResponse(responseCode = "404", description = "Не найдено такого объявления")
    })
    void updateAuto(@PathVariable("car_id")
                    @Parameter(description = "id авто", required = true)
                    UUID carId,
                    @RequestBody
                    @Parameter(description = "данные об авто", required = true)
                    AutoRequestDto auto);

    @GetMapping("/myCars")
    @PreAuthorize("isAuthenticated()")
    String getMyAutoList(@RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size,
                         @RequestParam(required = false) String sort,
                         @RequestParam(required = false) String order,
                         @RequestParam(required = false) UUID brand_id,
                         Model model);
}

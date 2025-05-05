package com.technokratos.vcs2.api;

import com.technokratos.vcs2.model.dto.request.ReportRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/report")
@Tag(name = "ReportController",
    description = "Контроллер для жалоб")
public interface ReportApi {

    @PostMapping("/{autoId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Пожаловаться на объявление",
            description = "Пожаловаться на объявление. Нужно быть авторизованным!")
    @SecurityRequirement(name = "baseAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "404", description = "Нет такого объявления"),
            @ApiResponse(responseCode = "403", description = "Вы не авторизованы"),
            @ApiResponse(responseCode = "400", description = "Неверный формат UUID или ReportRequestDto")
    })
    void report(@PathVariable("autoId")
                @Parameter(description = "id автомобиля", required = true)
                UUID autoId,
                @RequestBody
                @Parameter(description = "текст жалобы. Может прийти 400 статусный код, если пустой текст!")
                ReportRequestDto reportRequestDto);


    @PostMapping("/addView/{reportId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "baseAuth")
    @Operation(summary = "Просмотреть жалобу",
                description = "Добавить просмотр этой жалобы (Нужны хотя бы права администратора)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "404", description = "Нет такого объявления"),
            @ApiResponse(responseCode = "403", description = "Вы не авторизованы или у вас недостаточно прав"),
            @ApiResponse(responseCode = "400", description = "Неверный формат UUID")
    })
    void addView(@PathVariable("reportId")
                 @Parameter(description = "id жалобы", required = true)
                 UUID reportId);

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    String listOfReports(Model model,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size);


    @DeleteMapping("/{reportId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "baseAuth")
    @Operation(summary = "Удалить жалобу",
            description = "Удалить жалобу с таким id. Нужны хотя бы права модератора!")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "404", description = "Нет такого объявления"),
            @ApiResponse(responseCode = "403", description = "Вы не авторизованы или у вас недостаточно прав"),
            @ApiResponse(responseCode = "400", description = "Неверный формат UUID")
    })
    void deleteReport(@PathVariable("reportId")
                      @Parameter(description = "id жалобы", required = true)
                      UUID reportId);
}

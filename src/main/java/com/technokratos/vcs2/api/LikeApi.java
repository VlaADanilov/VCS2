package com.technokratos.vcs2.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "LikeController",
        description = "Контроллер для запросов, связанных с избранным")
@RequestMapping("/like")
public interface LikeApi {
    @GetMapping
    String like(Model model,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String sort,
                       @RequestParam(required = false) String order,
                       @RequestParam(required = false) UUID brand_id);

    @PostMapping("/{auto_id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Добавить лайк к авто",
            description = """
    Добавить лайк к авто с id, переданным через путь. Нужно быть авторизированным!""")
    @SecurityRequirement(name = "baseAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "404", description = "Нет авто с таким id"),
            @ApiResponse(responseCode = "403", description = "Вы не вошли в систему"),
            @ApiResponse(responseCode = "400", description = "Неверный формат UUID или лайк уже поставлен")
    })
    void addLike(@PathVariable("auto_id")
                 @Parameter(name = "id автомобиля", required = true)
                 UUID auto_id);

    @DeleteMapping("/{auto_id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "baseAuth")
    @Operation(summary = "Удалить лайк",
            description = "Удалить лайк с авто с переданным id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "404", description = "Нет авто с таким id"),
            @ApiResponse(responseCode = "403", description = "Вы не вошли в систему"),
            @ApiResponse(responseCode = "400", description = "Неверный формат UUID или лайка нет")
    })
    void deleteLike(@PathVariable("auto_id")
                    @Parameter(name = "id автомобиля", required = true)
                    UUID auto_id);
}

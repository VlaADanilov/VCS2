package com.technokratos.vcs2.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/admin")
@Tag(name = "AdminController",
    description = "Контроллер, связанный с возможностью администратора (Нужны права администратора)")
public interface AdminApi {
    @PostMapping("/{user_id}/doModerator")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "baseAuth")
    @Operation(summary = "Сделать пользователя модератором",
                description = "Сделать пользователя с переданным id модератором ")
    @ApiResponses(
            {@ApiResponse(responseCode = "200", description = "Успешно выполнено"),
                    @ApiResponse(responseCode = "404", description = "Пользователя с таким id е существует"),
                    @ApiResponse(responseCode = "403", description = "У вас нет прав администратора"),
                    @ApiResponse(responseCode = "400", description = "UUID не корректный")
            }
    )
    void doModerator(@PathVariable("user_id")
                     @Parameter(description = "id пользователя в формате UUID")
                     UUID userId);

    @Operation(summary = "Сделать пользователя обычным",
            description = "Сделать пользователя с переданным id обычным ")
    @ApiResponses(
            {@ApiResponse(responseCode = "200", description = "Успешно выполнено"),
                    @ApiResponse(responseCode = "404", description = "Пользователя с таким id е существует"),
                    @ApiResponse(responseCode = "403", description = "У вас нет прав администратора"),
                    @ApiResponse(responseCode = "400", description = "UUID не корректный")
            }
    )
    @PostMapping("/{user_id}/doUser")
    @ResponseBody
    @SecurityRequirement(name = "baseAuth")
    @ResponseStatus(HttpStatus.OK)
    void doDefault(@PathVariable("user_id")
                   @Parameter(description = "id пользователя в формате UUID")
                   UUID userId);
}

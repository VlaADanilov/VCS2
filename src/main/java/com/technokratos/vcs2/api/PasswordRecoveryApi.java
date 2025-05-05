package com.technokratos.vcs2.api;

import com.technokratos.vcs2.model.dto.request.EmailForRecoveryRequest;
import com.technokratos.vcs2.model.dto.request.EmailWithCodeAndNewPasswordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/password")
@Tag(name = "PasswordRecoveryController", description = "Контроллер восстановления пароля")
public interface PasswordRecoveryApi {

    @GetMapping
    String getPasswordRecoveryForm(Model model);

    @PostMapping
    @Operation(summary = "Отправить смс на email",
            description = "Отправить смс на email с кодом для восстановления пароля")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно отправлено"),
            @ApiResponse(responseCode = "400", description = "Неверный формат email"),
            @ApiResponse(responseCode = "404", description = "Не найдено пользователя с таким email")
    })
    void sendEmail(@RequestBody()
                   @Parameter(description = "email для восстановления", required = true)
                   EmailForRecoveryRequest email);

    @PostMapping("/recovery")
    @Operation(summary = "Изменить пароль",
            description = "Изменит пароль в случае, если отправят корректный код")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неверный формат email или пароля"),
            @ApiResponse(responseCode = "403", description = "Код не совпадает")
    })
    @ResponseBody
    void recovery(@RequestBody()
                   @Parameter(description = "email для восстановления", required = true)
                  EmailWithCodeAndNewPasswordDto email);
}

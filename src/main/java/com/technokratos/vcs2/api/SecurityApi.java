package com.technokratos.vcs2.api;

import com.technokratos.vcs2.model.dto.request.RegisterUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Tag(name = "SecurityController",
    description = "Контроллер для регистрации и авторизации")
public interface SecurityApi {

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    String login(@RequestParam(value = "error", required = false) String error,
                 Model model
    );

    @GetMapping("/registration")
    @PreAuthorize("isAnonymous()")
    String registration(Model model);

    @PostMapping("/registration")
    @PreAuthorize("isAnonymous()")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Регистрация",
            description = "Регистрация. Необходимо, чтобы не были авторизованны!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Читайте то, что вам пришло")
    })
    void addUser(@RequestBody
                 @Parameter(description = "Данные для регистрации", required = true)
                 RegisterUserDto regDto);
}

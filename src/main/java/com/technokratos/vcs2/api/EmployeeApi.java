package com.technokratos.vcs2.api;

import com.technokratos.vcs2.model.dto.request.EmployeeRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/employee")
public interface EmployeeApi {

    @GetMapping
    String getEmployee(Model model);

    @GetMapping("/add")
    String getAddEmployeeForm();

    @DeleteMapping("/{emp_id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @SecurityRequirement(name = "baseAuth")
    @Operation(summary = "Удалить сотрудника",
            description = "Удалить сотрудника по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "404", description = "Не найдено сотрудника"),
            @ApiResponse(responseCode = "400", description = "UUID формат некорректен"),
            @ApiResponse(responseCode = "403", description = "У вас нет прав")
    })
    void deleteEmployee(@PathVariable("emp_id")
                        @Parameter(description = "id сотрудника", required = true)
                        UUID emp_id);

    @PostMapping()
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "baseAuth")
    @Operation(summary = "Добавить сотрудника")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Данные некорректны"),
            @ApiResponse(responseCode = "403", description = "У вас нет прав")
    })
    void addEmployee(@RequestBody
                     @Parameter(description = "Информация о сотруднике", required = true)
                     EmployeeRequestDto employee);
}

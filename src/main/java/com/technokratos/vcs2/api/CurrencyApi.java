package com.technokratos.vcs2.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@RequestMapping("/currency")
@Tag(name = "CurrencyController",
    description = "Контроллер, связанный с валютами")
public interface CurrencyApi {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить названия всех доступных валют",
            description = "Возвращает список всех валют, которые поддерживает сервис")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Успешно вернул список"),
            @ApiResponse(responseCode = "500",
            description = "Api не доступно")
    })
    List<String> getAllCurrencies();

    @Operation(summary = "Получить в другой валюте",
            description = "Конвертировать сумму в рублях к валюте")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Успешно вернул список"),
            @ApiResponse(responseCode = "500",
                    description = "Api не доступно"),
            @ApiResponse(responseCode = "400",
                    description = "Не передали нужные параметры или amount не в формате числа")
    })
    @GetMapping("/toOtherCurrency")
    @ResponseStatus(HttpStatus.OK)
    Double getOtherCurrency(@RequestParam("currency")
                            @Parameter(description = "Сокращённое название валюты",
                                        example = "RUB",
                                        required = true)
                            String currency,
                            @RequestParam("amount")
                            @Parameter(description = "Сумма в рублях",
                                    example = "1000",
                                    required = true)
                            Integer amount);
}

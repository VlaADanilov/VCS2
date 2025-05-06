package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.api.CurrencyApi;
import com.technokratos.vcs2.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CurrencyController implements CurrencyApi {
    private final CurrencyService currencyService;


    public List<String> getAllCurrencies() {
        log.info("getAllCurrencies request");
        return currencyService.getAllCurrenciesNames();
    }

    public Double getOtherCurrency(String currency, Integer amount) {
        log.info("getOtherCurrency request with params: {},{}", currency, amount);
        return currencyService.getPrice(currency, amount);
    }
}

package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.api.CurrencyApi;
import com.technokratos.vcs2.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CurrencyController implements CurrencyApi {
    private final CurrencyService currencyService;


    public List<String> getAllCurrencies() {
        return currencyService.getAllCurrenciesNames();
    }

    public Double getOtherCurrency(String currency, Integer amount) {
        return currencyService.getPrice(currency, amount);
    }
}

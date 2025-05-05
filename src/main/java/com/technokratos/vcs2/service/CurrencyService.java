package com.technokratos.vcs2.service;

import java.util.List;

public interface CurrencyService {
    double getPrice(String currency, Integer amount);

    List<String> getAllCurrenciesNames();
}

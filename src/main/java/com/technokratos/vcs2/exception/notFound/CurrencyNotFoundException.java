package com.technokratos.vcs2.exception.notFound;

public class CurrencyNotFoundException extends NotFoundException {
    public CurrencyNotFoundException(String currency) {
        super("Currency %s not found".formatted(currency), "/");
    }
}

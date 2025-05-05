package com.technokratos.vcs2.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technokratos.vcs2.exception.ServiceException;
import com.technokratos.vcs2.exception.notFound.CurrencyNotFoundException;
import com.technokratos.vcs2.model.entity.Currency;
import com.technokratos.vcs2.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    private LocalDateTime lastUpdate;
    private List<String> allCurrencies;
    private final ObjectMapper mapper;
    @Override
    public double getPrice(String currency, Integer amount) {
        if (lastUpdate == null || Duration.between(lastUpdate, LocalDateTime.now()).toMinutes() > 90) {
            updateBase();
        }
        Optional<Currency> byId = currencyRepository.findById(currency);
        if (byId.isEmpty()) {
            throw new CurrencyNotFoundException(currency);
        } else {
            Currency cur = byId.get();
            return amount * cur.getPriceToOneRub();
        }
    }

    private void updateBase() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.exchangerate-api.com/v4/latest/RUB"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                allCurrencies = new ArrayList<>();
                String jsonBody = response.body();

                JsonNode root = mapper.readTree(jsonBody);

                JsonNode rates = root.get("rates");

                if (rates.isObject()) {
                    for (Iterator<Map.Entry<String, JsonNode>> it = rates.fields(); it.hasNext(); ) {
                        var entry = it.next();
                        String currency = entry.getKey();
                        double value = entry.getValue().asDouble();
                        allCurrencies.add(currency);

                        currencyRepository.save(new Currency(currency, value));
                    }
                } else {
                    System.out.println("rates не является объектом");
                }

                lastUpdate = LocalDateTime.now();
            } else {
                System.err.println("Ошибка: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            throw new ServiceException("Error in service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<String> getAllCurrenciesNames() {
        if (allCurrencies == null || lastUpdate == null || Duration.between(lastUpdate, LocalDateTime.now()).toMinutes() > 90) {
            updateBase();
        }
        return allCurrencies;
    }
}

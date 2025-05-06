package com.technokratos.vcs2.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technokratos.vcs2.exception.ServiceException;
import com.technokratos.vcs2.exception.notFound.CurrencyNotFoundException;
import com.technokratos.vcs2.model.entity.Currency;
import com.technokratos.vcs2.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    private LocalDateTime lastUpdate;
    private List<String> allCurrencies;
    private final ObjectMapper mapper;

    @Override
    public double getPrice(String currency, Integer amount) {
        log.info("Getting price for {} amount of currency {}", amount, currency);

        if (lastUpdate == null || Duration.between(lastUpdate, LocalDateTime.now()).toMinutes() > 90) {
            log.info("Currency data is outdated or not initialized. Starting update.");
            updateBase();
        }

        Optional<Currency> byId = currencyRepository.findById(currency);
        if (byId.isEmpty()) {
            log.warn("Currency {} not found in database", currency);
            throw new CurrencyNotFoundException(currency);
        } else {
            Currency cur = byId.get();
            log.info("Calculated price: {} * {} = {}", amount, cur.getPriceToOneRub(), amount * cur.getPriceToOneRub());
            return amount * cur.getPriceToOneRub();
        }
    }

    private void updateBase() {
        log.info("Starting to update currency exchange rates from external API");

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.exchangerate-api.com/v4/latest/RUB"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("Received HTTP response with status code: {}", response.statusCode());

            if (response.statusCode() == 200) {
                String jsonBody = response.body();
                log.debug("Response body: {}", jsonBody);

                JsonNode root = mapper.readTree(jsonBody);
                JsonNode rates = root.get("rates");

                if (!rates.isObject()) {
                    log.error("Field 'rates' is not an object");
                    System.out.println("rates не является объектом");
                    return;
                }

                allCurrencies = new ArrayList<>();
                int savedCount = 0;

                for (Iterator<Map.Entry<String, JsonNode>> it = rates.fields(); it.hasNext(); ) {
                    var entry = it.next();
                    String currencyCode = entry.getKey();
                    double value = entry.getValue().asDouble();

                    allCurrencies.add(currencyCode);
                    currencyRepository.save(new Currency(currencyCode, value));
                    savedCount++;
                }

                log.info("Saved {} currencies into the database", savedCount);
                lastUpdate = LocalDateTime.now();
                log.info("Currency base updated successfully at {}", lastUpdate);

            } else {
                log.error("Failed to fetch currency data. Status code: {}", response.statusCode());
                System.err.println("Ошибка: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            log.error("Error while updating currency data", e);
            throw new ServiceException("Error in service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<String> getAllCurrenciesNames() {
        log.info("Fetching list of all currencies");

        if (allCurrencies == null || lastUpdate == null || Duration.between(lastUpdate, LocalDateTime.now()).toMinutes() > 90) {
            log.info("Currency list is outdated or empty. Updating...");
            updateBase();
        }

        log.info("Found {} currencies", allCurrencies != null ? allCurrencies.size() : 0);
        return allCurrencies;
    }
}

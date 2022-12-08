package com.malibu.pwc.service;

import com.malibu.pwc.model.Country;
import com.malibu.pwc.model.dto.CountryDTO;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CountryDataProvider {


    private static final Logger LOG = LoggerFactory.getLogger(CountryService.class);
    private final String uri = "https://raw.githubusercontent.com/mledoze/countries/master/countries.json";
    private final RestTemplate restTemplate;

    private List<Country> countryList = new ArrayList<>();

    public CountryDataProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void fetchCountryList() {
        Runnable runnable = () -> {
            try {
                ResponseEntity<CountryDTO[]> response = restTemplate.getForEntity(uri, CountryDTO[].class);
                countryList = Arrays.stream(response.getBody())
                        .filter(country -> !country.getBorders().isEmpty())
                        .map(countryDTO -> new Country(countryDTO.getBorders(), countryDTO.getCca3()))
                        .collect(Collectors.toList());
                LOG.info("Country JSON Data could be fetched successfully.");
            } catch (Exception e) {
                LOG.error("Country JSON Data could not be fetched.", e);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Cacheable(value = "COUNTRY_LIST", unless = "#result == null OR #result.size() == 0")
    public Map<String, Country> getCountryList() {
        return countryList.stream().collect(Collectors.toMap(Country::getName, Function.identity()));
    }
}

package com.malibu.pwc.service;

import com.malibu.pwc.exception.ResourceNotFoundException;
import com.malibu.pwc.exception.RuleException;
import com.malibu.pwc.model.Country;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

@SpringBootTest
class CountryServiceTest {

    @Autowired
    CountryService countryService;
    @MockBean
    CountryDataProvider dataProvider;

    Map<String, Country> testData = new HashMap<>();


    @BeforeEach
    void setUp() {
        testData.put("BLM", new Country(new ArrayList<>(), "BLM"));
        testData.put("PNG", new Country(List.of("IDN"), "PNG"));
        testData.put("IDN", new Country(Arrays.asList("TLS", "MYS", "PNG"), "IDN"));
        testData.put("CAF", new Country(Arrays.asList("CMR", "TCD", "COD", "COG", "SSD", "SDN"), "CAF"));
        testData.put("SDN", new Country(Arrays.asList("CAF", "TCD", "EGY", "ERI", "ETH", "LBY", "SSD"), "SDN"));
    }

    @Test
    void whenEmptyOrigin_throwRuleException() {
        var ex = Assertions.assertThrows(RuleException.class,
                () -> {
                    countryService.getPath(null, "anything");
                    countryService.getPath("", "anything");
                    countryService.getPath("   ", "anything");
                });
        Assertions.assertEquals("Origin cannot be empty or blank.", ex.getMessage());
    }

    @Test
    void whenEmptyDestination_throwRuleException() {
        var ex = Assertions.assertThrows(RuleException.class, () -> {
            countryService.getPath("anything", null);
            countryService.getPath("anything", "");
            countryService.getPath("anything", "  ");
        });
        Assertions.assertEquals("Destination cannot be empty or blank", ex.getMessage());
    }

    @Test
    void whenOriginEqualsDestination_throwRuleException() {
        var ex = Assertions.assertThrows(RuleException.class, () -> {
            countryService.getPath("TEST", "TEST");
        });
        Assertions.assertEquals("Origin and Destination are the same.", ex.getMessage());
    }

    @Test
    void whenOriginNotFound_throwResourceNotFoundException() {
        Mockito.when(dataProvider.getCountryList()).thenReturn(testData);
        var ex = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            countryService.getPath("TEST", "BLM");
        });
        Assertions.assertEquals("Origin %s is not found in source list.".formatted("TEST"), ex.getMessage());
    }

    @Test
    void whenDestinationBorderEmpty_throwResourceNotFoundException() {
        Mockito.when(dataProvider.getCountryList()).thenReturn(testData);
        String origin = "BLM"; // BLM doesn't have any border.
        var ex = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            countryService.getPath(origin, "PNG");
        });
        Assertions.assertEquals("Origin(%s) has no any border to reach destination.".formatted(origin), ex.getMessage());
    }

    @Test
    void whenDestinationNotFound_throwResourceNotFoundException() {
        Mockito.when(dataProvider.getCountryList()).thenReturn(testData);
        String origin = "IDN";  // Not null, has borders
        String destination = "TEST"; // Null
        var ex = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            countryService.getPath(origin, destination);
        });
        Assertions.assertEquals("Destination(%s) is not found in source list.".formatted(destination), ex.getMessage());
    }
}
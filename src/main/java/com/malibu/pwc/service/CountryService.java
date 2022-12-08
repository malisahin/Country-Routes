package com.malibu.pwc.service;

import com.malibu.pwc.exception.PathNotFoundException;
import com.malibu.pwc.exception.ResourceNotFoundException;
import com.malibu.pwc.exception.RuleException;
import com.malibu.pwc.model.Country;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CountryService {

    private final CountryDataProvider countryDataProvider;

    public Map<String, Country> getRoute() {
        return countryDataProvider.getCountryList();
    }

    public final List<String> getPath(String originName, String destinationName) {

        final Map<String, Country> countries = countryDataProvider.getCountryList();

        if (originName == null || originName.isBlank()) {
            throw new RuleException("Origin cannot be empty or blank.");
        }
        Country origin = countries.get(originName);
        if (origin == null) {
            throw new ResourceNotFoundException("Origin %s is not found in source list.".formatted(originName));
        }
        if (origin.getBorders().isEmpty()) {
            throw new PathNotFoundException("Origin(%s) has no any border to reach destination.".formatted(originName));
        }

        if (destinationName == null || destinationName.isBlank()) {
            throw new RuleException("Destination cannot be empty or blank");
        }
        Country destination = countries.get(destinationName);
        if (destination == null) {
            throw new ResourceNotFoundException("Destination(%s) is not found in source list.".formatted(destinationName));
        }
        if (destination.getBorders().isEmpty()) {
            throw new PathNotFoundException("Destination(%s) has no any border. So origin(%s) can not reach destination point.".formatted(destinationName, originName));
        }


        return search(countries, origin, destination);
    }

    /**
     * @param countries
     * @param origin
     * @param destination
     * @return route
     * @apiNote Depth-First Traversal
     */
    private List<String> search(Map<String, Country> countries, Country origin, Country destination) {
        final Map<Country, Boolean> visited = new HashMap<>();
        final Map<Country, Country> previous = new HashMap<>();
        var currentCountry = origin;

        Queue<Country> pivot = new ArrayDeque<>();
        pivot.add(currentCountry);

        visited.put(currentCountry, true);

        OUTER:
        while (!pivot.isEmpty()) {
            currentCountry = pivot.remove();
            log.debug("Visiting " + currentCountry.getName());
            if (currentCountry.equals(destination)) {
                log.debug("Origin and destination are equal");
                break;
            } else {
                for (var neighbour : currentCountry.getBorders()) {
                    var neighbourCountry = countries.get(neighbour);
                    if (!visited.containsKey(neighbourCountry)) {
                        log.debug("... registering neighbour " + neighbourCountry.getName());
                        pivot.add(neighbourCountry);
                        visited.put(neighbourCountry, true);
                        previous.put(neighbourCountry, currentCountry);
                        if (neighbourCountry.equals(destination)) {
                            log.debug("Shortest path found");
                            currentCountry = neighbourCountry;
                            break OUTER;
                        }
                    } else {
                        log.debug("... skipping neighbour " + neighbourCountry.getName());
                    }
                }
            }
        }

        if (!currentCountry.equals(destination)) {
            throw new PathNotFoundException("Cannot reach the path");
        }

        List<Country> path = new ArrayList<>();
        for (Country node = destination; node != null; node = previous.get(node)) {
            path.add(node);
        }

        return path.stream()
                .map(Country::getName)
                .collect(Collectors.toList());
    }


}

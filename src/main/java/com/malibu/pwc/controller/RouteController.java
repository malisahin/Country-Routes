package com.malibu.pwc.controller;

import com.malibu.pwc.model.Country;
import com.malibu.pwc.model.dto.RouteDTO;
import com.malibu.pwc.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/routing")
public class RouteController {

    @Autowired
    private CountryService countryService;

    @GetMapping(value = "/{origin}/{destination}")
    public ResponseEntity<RouteDTO> getRoute(@PathVariable("origin") String origin, @PathVariable("destination") String destination) {
        System.out.println("Origin: " + origin + " Destination: " + destination);
        List<String> routeList = countryService.getPath(origin, destination);
        return new ResponseEntity<>(new RouteDTO(routeList), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Map<String, Country>> countryList() {
        return new ResponseEntity<>(countryService.getList(), HttpStatus.OK);
    }
}


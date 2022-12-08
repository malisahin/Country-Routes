package com.malibu.pwc.controller;

import com.malibu.pwc.model.Country;
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
@RequestMapping("/country")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping(value = "/{origin}/{destination}")
    public ResponseEntity<List<String>> getRoute(@PathVariable("origin") String origin, @PathVariable("destination") String destination) {

        System.out.println("Origin: " + origin + " Destination: " + destination);
        return new ResponseEntity<>(countryService.getPath(origin, destination), HttpStatus.BAD_REQUEST);
    }

    @GetMapping()
    public String test() {
        return "Hello World";
    }
}


package com.malibu.pwc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Country {

    private List<String> borders;
    private String name;
}

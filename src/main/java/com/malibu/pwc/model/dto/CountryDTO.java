package com.malibu.pwc.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Data
public class CountryDTO implements Serializable {


    private List<String> borders;
    private String cca3;
}

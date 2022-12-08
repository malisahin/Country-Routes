package com.malibu.pwc.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CountryGraph implements Serializable {

    private Map<Country, List<Country>> map;

}

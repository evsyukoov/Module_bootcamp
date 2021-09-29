package com.evsyukoov.module.service;

import com.evsyukoov.module.access.CityDao;
import com.evsyukoov.module.entities.City;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CityService {
    CityDao cityDao;

    public CityService(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    public String getMaxPopulation() {
        City cityWithMaxPopulation = Collections.max(cityDao.getAllEntities(), (City a, City b)
                -> Integer.compare(a.getPopulation(), b.getPopulation()));
        return String.format("[%d] - %d", cityWithMaxPopulation.getId(), cityWithMaxPopulation.getPopulation());
    }

    public List<String> getRegions() {
        List<City> cities = cityDao.getAllEntities();
        List<String> result = new ArrayList<>();
        cities.stream()
                .collect(Collectors.groupingBy(City::getRegion, Collectors.counting()))
                .forEach((k, v) ->
                        result.add(String.format("%s - %d", k, v)));
        return result;
    }
}

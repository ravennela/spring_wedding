package com.example.online.location.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.online.location.dto.CityResponse;
import com.example.online.location.enitity.City;
import com.example.online.location.repository.CityRepository;


@Service
public class CityServiceImpl implements CityService {
    @Autowired
    private  CityRepository cityRepository;

  

    @Override
    public Page<CityResponse> getCities(String search, Pageable pageable) {

        Page<City> cityPage;

        if (search != null && !search.trim().isEmpty()) {
            cityPage = cityRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            cityPage = cityRepository.findAll(pageable);
        }

        return cityPage.map(
            city -> new CityResponse(city.getId(), city.getName())
        );
    }
}

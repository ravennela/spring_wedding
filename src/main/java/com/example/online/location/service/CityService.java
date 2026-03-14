package com.example.online.location.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.online.location.dto.CityResponse;

public interface CityService {

    Page<CityResponse> getCities(String search, Pageable pageable);
}


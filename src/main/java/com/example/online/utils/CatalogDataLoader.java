package com.example.online.utils;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.online.event.entity.Decoration;
import com.example.online.event.entity.EventType;
import com.example.online.event.repository.DecorationRepository;
import com.example.online.event.repository.EventTypeRepository;
import com.example.online.location.enitity.City;
import com.example.online.location.repository.CityRepository;

@Component
public class CatalogDataLoader implements CommandLineRunner {

    private  EventTypeRepository eventTypeRepository;
    private  CityRepository cityRepository;
    private  DecorationRepository decorationRepository;

    public CatalogDataLoader(
            EventTypeRepository eventTypeRepository,
            CityRepository cityRepository,
            DecorationRepository decorationRepository) {
        this.eventTypeRepository = eventTypeRepository;
        this.cityRepository = cityRepository;
        this.decorationRepository = decorationRepository;
    }

    @Override
    public void run(String... args) {

        
        // Decorations are usually created by admin (can seed sample)
    }
}
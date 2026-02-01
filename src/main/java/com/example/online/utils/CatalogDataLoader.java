package com.example.online.utils;

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

        if (eventTypeRepository.count() == 0) {
            EventType type=new EventType();
            type.setName("Wedding");

             EventType type2=new EventType();
            type2.setName("Birthday");

            eventTypeRepository.save(type);
            eventTypeRepository.save(type2);
        }

        if (cityRepository.count() == 0) {
            City city=new City();
            city.setName("Hyderabad");

            City city2=new City();
            city2.setName("Delhi");

            cityRepository.save(city);
            cityRepository.save(city2);
        }



        if (decorationRepository.count() > 0) {
            return;
        }

        // EventType wedding = eventTypeRepository.findByName("Wedding")
        //         .orElseThrow();

        City hyderabad = cityRepository.findByName("Hyderabad")
                .orElseThrow();

        Decoration royalWedding = new Decoration();
       // royalWedding.setTitle("Royal Wedding Decoration");
        royalWedding.setDescription("Premium stage, floral decor, lighting");
        //royalWedding.setBasePrice(75000);
        //royalWedding.setEventType(wedding);
        royalWedding.setCity(hyderabad);
        royalWedding.setActive(true);

        decorationRepository.save(royalWedding);
    }
        // Decorations are usually created by admin (can seed sample)
    }

package com.example.online.catlog.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.online.catlog.dto.DecorationResponseDTO;
import com.example.online.event.entity.Decoration;
import com.example.online.event.entity.EventType;
import com.example.online.event.repository.DecorationRepository;
import com.example.online.event.repository.EventTypeRepository;
import com.example.online.location.enitity.City;
import com.example.online.location.repository.CityRepository;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    private final EventTypeRepository eventTypeRepository;
    private final CityRepository cityRepository;
    private final DecorationRepository decorationRepository;

    public CatalogController(
            EventTypeRepository eventTypeRepository,
            CityRepository cityRepository,
            DecorationRepository decorationRepository) {
        this.eventTypeRepository = eventTypeRepository;
        this.cityRepository = cityRepository;
        this.decorationRepository = decorationRepository;
    }

    // ---------------- EVENT TYPES ----------------

    @GetMapping("/event-types")
    public List<EventType> getEventTypes() {
        return eventTypeRepository.findByIsActiveTrue();
    }

    // ---------------- CITIES ----------------

    @GetMapping("/cities")
    public List<City> getCities() {
        return cityRepository.findByIsActiveTrue();
    }

    // ---------------- DECORATIONS ----------------

    // @GetMapping("/decorations")
    // public List<DecorationResponseDTO> getDecorations(
    //         @RequestParam UUID eventTypeId,
    //         @RequestParam UUID cityId) {

    //     return decorationRepository
    //             .findByEventTypeIdAndCityIdAndIsActiveTrue(eventTypeId, cityId)
    //             .stream()
    //             .map(decoration -> {
    //                 DecorationResponseDTO dto = new DecorationResponseDTO();
    //                 dto.setId(decoration.getId());
    //                 // dto.setTitle(decoration.getTitle());
    //                 // dto.setBasePrice(decoration.getBasePrice());
    //                 // dto.setEventType(decoration.getEventType().getName());
    //                 // dto.setCity(decoration.getCity().getName());
    //                 return dto;
    //             })
    //             .toList();
    // }
}
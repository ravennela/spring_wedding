package com.example.online.publicapi.event.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.online.publicapi.event.dto.PublicEventTypeDto;
import com.example.online.publicapi.event.service.EventService;

@RestController
@RequestMapping("/api/public/events")
public class PublicEventController {
    @Autowired
    private EventService eventService;

    @GetMapping
    public List<PublicEventTypeDto> getAllEventTypes(Boolean active) {
        return eventService.getAllEventTypes(active);
    }
}

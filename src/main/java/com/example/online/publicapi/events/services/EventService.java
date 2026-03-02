package com.example.online.publicapi.events.services;

import java.util.List;

import com.example.online.publicapi.events.dto.PublicEventTypeDto;

public interface EventService {

    List<PublicEventTypeDto> getAllEventTypes();
}

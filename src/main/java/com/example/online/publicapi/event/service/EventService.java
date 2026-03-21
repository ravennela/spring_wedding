package com.example.online.publicapi.event.service;

import java.util.List;

import com.example.online.publicapi.event.dto.PublicEventTypeDto;

public interface EventService {

    List<PublicEventTypeDto> getAllEventTypes(Boolean active);
}

package com.example.online.event.service;

import org.springframework.data.domain.Page;

import com.example.online.event.dto.CreateEventTypeRequest;
import com.example.online.event.dto.EventTypeResponse;
import com.example.online.event.entity.EventType;

public interface EventTypeService {

    EventTypeResponse createEventType(CreateEventTypeRequest request);
      Page<EventType> getEventTypes(
            int page,
            int size,
            String search,
            String sortBy,
            String sortDir
    );
}

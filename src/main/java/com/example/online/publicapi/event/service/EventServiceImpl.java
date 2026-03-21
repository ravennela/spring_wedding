package com.example.online.publicapi.event.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.online.event.entity.EventType;
import com.example.online.event.repository.EventTypeRepository;
import com.example.online.publicapi.event.dto.PublicEventTypeDto;

@Service
public class EventServiceImpl implements EventService {

    private final EventTypeRepository eventTypeRepository;

    public EventServiceImpl(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository;
    }

    @Override
    public List<PublicEventTypeDto> getAllEventTypes(Boolean active) {

        List<EventType> eventTypes = eventTypeRepository.findByIsActive(active != null ? active : true  );

        return eventTypes.stream()
                .map(event -> new PublicEventTypeDto(
                        event.getId(),
                        event.getName(),
                        event.getIconUrl() // imageUrl (add when image support is implemented)
                ))
                .collect(Collectors.toList());
    }
}

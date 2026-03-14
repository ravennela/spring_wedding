package com.example.online.event.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.online.event.dto.CreateEventTypeRequest;
import com.example.online.event.dto.EventTypeResponse;
import com.example.online.event.dto.UpdateEventTypeRequest;
import com.example.online.event.entity.EventType;
import com.example.online.event.repository.EventTypeRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class EventTypeServiceImpl implements EventTypeService {

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Override
    public EventTypeResponse createEventType(CreateEventTypeRequest request) {

        // 1️⃣ Duplicate check
        if (eventTypeRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Event type already exists");
            // replace with your custom BadRequestException if you have one
        }

        // 2️⃣ Map DTO → Entity
        EventType eventType = new EventType();
        eventType.setName(request.getName());
        eventType.setDescription(request.getDescription());
        eventType.setIconUrl(request.getIconUrl());
        eventType.setSortOrder(request.getSortOrder());
        eventType.setActive(true);

        // 3️⃣ Save
        EventType saved = eventTypeRepository.save(eventType);

        // 4️⃣ Map Entity → Response DTO
        return mapToResponse(saved);
    }

    private EventTypeResponse mapToResponse(EventType eventType) {
        EventTypeResponse response = new EventTypeResponse();
        response.setId(eventType.getId().toString());
        response.setName(eventType.getName());
        response.setDescription(eventType.getDescription());
        response.setIconUrl(eventType.getIconUrl());
        response.setSortOrder(eventType.getSortOrder());
        response.setActive(eventType.isActive());
        return response;
    }

    @Override
    public Page<EventType> getEventTypes(
            int page,
            int size,
            String search,
            String sortBy,
            String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        if (search == null || search.trim().isEmpty()) {
            return eventTypeRepository.findAll(pageable);
        }

        return eventTypeRepository.findByNameContainingIgnoreCase(
                search.trim(),
                pageable);
    }

    @Override
    public EventTypeResponse updateEventType(UUID id, UpdateEventTypeRequest request) {
        EventType eventType = eventTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event type not found"));
        eventType.setName(request.getName());
        eventType.setDescription(request.getDescription());
        eventType.setIconUrl(request.getImageUrl());
        System.out.println("Setting isActive to: " + request.isActive());
        eventType.setActive(request.isActive());

        EventType updated = eventTypeRepository.save(eventType);

        return mapToResponse(updated);
    }

    @Override
    public EventTypeResponse inctivateEventType(UUID id) {
        EventType eventType = eventTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event type not found"));
        eventType.setActive(false);
        EventType updated = eventTypeRepository.save(eventType);

        return mapToResponse(updated);
    }

    @Override
    public EventTypeResponse getEventTypeById(UUID id) {

        EventType eventType = eventTypeRepository
                .findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new IllegalArgumentException("Event type not found"));

        return mapToResponse(eventType);
    }
}

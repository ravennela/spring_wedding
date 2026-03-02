package com.example.online.event.controller;

import com.example.online.event.dto.CreateEventTypeRequest;
import com.example.online.event.dto.EventTypeListItemResponse;
import com.example.online.event.dto.EventTypeResponse;
import com.example.online.event.dto.PagedResponse;
import com.example.online.event.dto.UpdateEventTypeRequest;
import com.example.online.event.entity.EventType;
import com.example.online.event.service.EventTypeService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/event-types")
public class EventTypeController {

    @Autowired
    private EventTypeService eventTypeService;

    // ➕ Create Event Type
    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createEventType(
            @Valid @RequestBody CreateEventTypeRequest request) {

        EventTypeResponse response = eventTypeService.createEventType(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping
    public ResponseEntity<?> getEventTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Page<EventType> eventTypePage = eventTypeService.getEventTypes(
                page,
                size,
                search,
                sortBy,
                sortDir
        );

        List<EventTypeListItemResponse> content = eventTypePage
                .getContent()
                .stream()
                .map(EventTypeListItemResponse::from)
                .collect(Collectors.toList());

        PagedResponse<EventTypeListItemResponse> response =
        new PagedResponse<>(
                content,
                eventTypePage.isLast(),
                eventTypePage.getNumber(),
                eventTypePage.getSize(),
                eventTypePage.getTotalElements(),
                eventTypePage.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEventType(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateEventTypeRequest request) {
                System.out.println("Received update request for ID: " + request.isActive());

        EventTypeResponse response = eventTypeService.updateEventType(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> inctivateEventType(@PathVariable UUID id) {
        EventTypeResponse response = eventTypeService.inctivateEventType(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventTypeById(@PathVariable UUID id) {
        EventTypeResponse response = eventTypeService.getEventTypeById(id);
        return ResponseEntity.ok(response);             
    }
}

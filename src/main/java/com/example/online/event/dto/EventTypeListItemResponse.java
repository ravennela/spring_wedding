package com.example.online.event.dto;

import java.time.LocalDateTime;

import com.example.online.event.entity.EventType;

import java.time.LocalDateTime;

public class EventTypeListItemResponse {

    private String id;
    private String name;
    private String description;
    private boolean active;
    private LocalDateTime createdAt;

    public EventTypeListItemResponse(
            String id,
            String name,
            String description,
            boolean active,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public static EventTypeListItemResponse from(EventType eventType) {
        return new EventTypeListItemResponse(
                eventType.getId().toString(),
                eventType.getName(),
                eventType.getDescription(),
                eventType.isActive(),
                eventType.getCreatedAt()
        );
    }
}


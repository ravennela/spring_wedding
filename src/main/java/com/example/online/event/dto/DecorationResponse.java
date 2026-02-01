package com.example.online.event.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class DecorationResponse {

    private UUID id;
    private String name;
    private String description;
    private String inclusions;
    private String exclusions;
    private BigDecimal basePrice;
    private boolean active;

    private UUID cityId;
    private String cityName;

    private UUID eventTypeId;
    private String eventTypeName;

    public DecorationResponse(
            UUID id,
            String name,
            String description,
            String inclusions,
            String exclusions,
            BigDecimal basePrice,
            boolean active,
            UUID cityId,
            String cityName,
            UUID eventTypeId,
            String eventTypeName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.inclusions = inclusions;
        this.exclusions = exclusions;
        this.basePrice = basePrice;
        this.active = active;
        this.cityId = cityId;
        this.cityName = cityName;
        this.eventTypeId = eventTypeId;
        this.eventTypeName = eventTypeName;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getInclusions() {
        return inclusions;
    }

    public String getExclusions() {
        return exclusions;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public boolean isActive() {
        return active;
    }

    public UUID getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public UUID getEventTypeId() {
        return eventTypeId;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }
}

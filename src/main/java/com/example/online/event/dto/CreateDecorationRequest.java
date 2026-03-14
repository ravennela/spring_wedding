package com.example.online.event.dto;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CreateDecorationRequest {

    private UUID eventTypeId;
    private UUID cityId;

    private String name;
    private String description;
    private String inclusions;
    private String exclusions;

    private BigDecimal basePrice;

    // image URLs or image identifiers
    private List<String> imageUrls;

    // optional – default true if not sent
    private Boolean active;

    public CreateDecorationRequest() {
    }

    // -------- Getters --------

    public UUID getEventTypeId() {
        return eventTypeId;
    }

    public UUID getCityId() {
        return cityId;
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public Boolean getActive() {
        return active;
    }
}


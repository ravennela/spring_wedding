package com.example.online.event.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateDecorationRequest {

    @NotBlank
    private String name;

    private String description;
    private String inclusions;
    private String exclusions;

    @NotNull
    private BigDecimal basePrice;

    @NotNull
    private UUID cityId;

    @NotNull
    private UUID eventTypeId;

    private boolean active;

    /**
     * When non-null, replaces all decoration_images for this decoration.
     * Empty list clears images. Null = leave images unchanged (backward compatible).
     */
    private List<DecorationImageRequest> images;

    // getters & setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInclusions() {
        return inclusions;
    }

    public void setInclusions(String inclusions) {
        this.inclusions = inclusions;
    }

    public String getExclusions() {
        return exclusions;
    }

    public void setExclusions(String exclusions) {
        this.exclusions = exclusions;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public UUID getCityId() {
        return cityId;
    }

    public void setCityId(UUID cityId) {
        this.cityId = cityId;
    }

    public UUID getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(UUID eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<DecorationImageRequest> getImages() {
        return images;
    }

    public void setImages(List<DecorationImageRequest> images) {
        this.images = images;
    }
}

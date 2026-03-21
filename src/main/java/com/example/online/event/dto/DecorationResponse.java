package com.example.online.event.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

    private List<DecorationImageResponse> images = new ArrayList<>();
    private List<String> imageUrls = new ArrayList<>();

    public DecorationResponse() {
    }

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
            String eventTypeName,
            List<DecorationImageResponse> images,
            List<String> imageUrls) {
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
        this.images = images != null ? images : new ArrayList<>();
        this.imageUrls = imageUrls != null ? imageUrls : new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public UUID getCityId() {
        return cityId;
    }

    public void setCityId(UUID cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public UUID getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(UUID eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public List<DecorationImageResponse> getImages() {
        return images;
    }

    public void setImages(List<DecorationImageResponse> images) {
        this.images = images != null ? images : new ArrayList<>();
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls != null ? imageUrls : new ArrayList<>();
    }
}

package com.example.online.publicapi.decoration.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class DecorationDetailDto {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private List<String> imageUrls;
    private String eventTypeName;
    private String cityName;
    private Integer durationHours;
    private Integer capacity;
    private String inclusions;
    private String exclusions;
    private UUID eventTypeId;
    private UUID cityId;

    public DecorationDetailDto(UUID id,
            String name,
            String description,
            BigDecimal price,
            List<String> imageUrls,
            String eventTypeName,
            String cityName,
            Integer durationHours,
            Integer capacity,
            String inclusions,
            String exclusions,
            UUID eventTypeId,
            UUID cityId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrls = imageUrls;
        this.eventTypeName = eventTypeName;
        this.cityName = cityName;
        this.durationHours = durationHours;
        this.capacity = capacity;
        this.inclusions = inclusions;
        this.exclusions = exclusions;
        this.eventTypeId = eventTypeId;
        this.cityId = cityId;
    }

    public UUID getId() {
        return id;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(Integer durationHours) {
        this.durationHours = durationHours;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public UUID getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(UUID eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public UUID getCityId() {
        return cityId;
    }

    public void setCityId(UUID cityId) {
        this.cityId = cityId;
    }
}

package com.example.online.publicapi.events.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class DecorationListItemDto {

    private UUID id;
    private String name;
    private BigDecimal price;
    private String thumbnailUrl;
    private String eventTypeName;
    private String cityName;


    public DecorationListItemDto(UUID id,
                                 String name,
                                 BigDecimal price,
                                 String thumbnailUrl,
                                 String eventTypeName,
                                 String cityName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.thumbnailUrl = thumbnailUrl;
        this.eventTypeName = eventTypeName;
        this.cityName = cityName;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
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
}
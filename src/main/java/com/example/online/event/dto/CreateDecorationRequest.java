package com.example.online.event.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateDecorationRequest {

    private UUID eventTypeId;
    private UUID cityId;
    private String name;
    private String description;
    private String inclusions;
    private String exclusions;
    private BigDecimal basePrice;

    /** Legacy: flat URL list */
    private List<String> imageUrls;

    /** Flutter / Cloudinary: [{ imageUrl, publicId }, ...] */
    private List<DecorationImageRequest> images = new ArrayList<>();

    private Boolean active;

    public CreateDecorationRequest() {
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<DecorationImageRequest> getImages() {
        return images;
    }

    public void setImages(List<DecorationImageRequest> images) {
        this.images = images != null ? images : new ArrayList<>();
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

package com.example.online.event.dto;

import java.util.UUID;

public class DecorationImageDto {
    private UUID id;
    private String imageUrl;

    public DecorationImageDto() {
    }

    public DecorationImageDto(UUID id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

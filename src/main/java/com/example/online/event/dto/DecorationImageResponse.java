package com.example.online.event.dto;

import java.util.UUID;

public class DecorationImageResponse {

    private UUID id;
    private String imageUrl;
    private String publicId;

    public DecorationImageResponse() {
    }

    public DecorationImageResponse(UUID id, String imageUrl, String publicId) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.publicId = publicId;
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

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
}

package com.example.online.event.dto;

public class UpdateEventTypeRequest {

    private String name;
    private String description;
    private String iconUrl;
    private String iconPublicId;
    private boolean active;

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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconPublicId() {
        return iconPublicId;
    }

    public void setIconPublicId(String iconPublicId) {
        this.iconPublicId = iconPublicId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}


package com.example.online.event.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateEventTypeRequest {

    @NotBlank(message = "Event type name is required")
    @Size(min = 2, max = 100, message = "Event type name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description can be max 500 characters")
    private String description;

    private String iconUrl;

    private String iconPublicId;
    

    private Integer sortOrder;
    

    public String getIconPublicId() {
        return iconPublicId;
    }
    public void setIconPublicId(String iconPublicId) {
        this.iconPublicId = iconPublicId;
    }
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
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
    public String getIconUrl() {
        return iconUrl;
    }
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    public Integer getSortOrder() {
        return sortOrder;
    }
   
}


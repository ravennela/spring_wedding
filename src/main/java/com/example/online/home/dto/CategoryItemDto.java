package com.example.online.home.dto;
import java.util.UUID;
public class CategoryItemDto {

    private UUID id;
    private String name;
    private String icon;
    private String imageUrl;
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
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public CategoryItemDto(UUID id, String name, String icon, String imageUrl) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.imageUrl = imageUrl;
    }
}
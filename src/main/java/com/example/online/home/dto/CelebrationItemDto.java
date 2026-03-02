package com.example.online.home.dto;

public class CelebrationItemDto {
    private String title;
    private String type;
    private String imageUrl;
    public CelebrationItemDto(String title, String type, String imageUrl) {
        this.title = title;
        this.type = type;
        this.imageUrl = imageUrl;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
}
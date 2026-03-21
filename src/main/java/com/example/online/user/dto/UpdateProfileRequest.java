package com.example.online.user.dto;

import java.util.UUID;

public class UpdateProfileRequest {
    private String name;
    private String email;
    private UUID cityId;

    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public UUID getCityId() {
        return cityId;
    }
    public void setCityId(UUID cityId) {
        this.cityId = cityId;
    }
    
}

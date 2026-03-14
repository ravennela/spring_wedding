package com.example.online.auth.dto;

public class AuthResponseDto {

    private String token;
    private String role;
    private String userId;
    private String name;

    public AuthResponseDto(String token, String role, String userId, String name) {
        this.userId = userId;
        this.name = name;
        this.token = token;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }
}

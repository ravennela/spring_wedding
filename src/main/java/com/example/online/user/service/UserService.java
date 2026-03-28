package com.example.online.user.service;

import java.util.UUID;

import com.example.online.user.dto.UpdateProfileRequest;
import com.example.online.user.dto.UserProfileDto;

public interface UserService {
    void updateProfile(UUID userId, UpdateProfileRequest request);

    boolean isProfileComplete(UUID userId);

    UserProfileDto getProfile(UUID userId);
}

package com.example.online.user.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.online.location.entity.City;
import com.example.online.location.repository.CityRepository;
import com.example.online.user.dto.UpdateProfileRequest;
import com.example.online.user.entity.User;
import com.example.online.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CityRepository cityRepository;

    UserServiceImpl(UserRepository userRepository, CityRepository cityRepository) {
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public void updateProfile(UUID userId, UpdateProfileRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setCity(city);

        userRepository.save(user);
    }

    @Override
    public boolean isProfileComplete(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return user.getName() != null && user.getCity() != null;
    }
}
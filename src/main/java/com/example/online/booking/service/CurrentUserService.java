package com.example.online.booking.service;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.online.user.entity.User;
import com.example.online.user.repository.UserRepository;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new RuntimeException("No authentication found");
        }

        Object principal = auth.getPrincipal();

        // 1. If principal is our CustomUserDetails, return user directly
        if (principal instanceof com.example.online.auth.security.CustomUserDetails) {
            return ((com.example.online.auth.security.CustomUserDetails) principal).getUser();
        }

        // 2. Fallback: try parsing name as UUID or look up by phone
        String name = auth.getName();
        try {
            return userRepository.findById(UUID.fromString(name))
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found by ID: " + name));
        } catch (IllegalArgumentException e) {
            // If name is not a UUID, check if it's a mobile number
            return userRepository.findByPhone(name)
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found by phone: " + name));
        }
    }
}
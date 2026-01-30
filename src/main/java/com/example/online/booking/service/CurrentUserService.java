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
        String userId  = auth.getName();

        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() ->
                        new RuntimeException("Logged-in user not found"));
    }
}
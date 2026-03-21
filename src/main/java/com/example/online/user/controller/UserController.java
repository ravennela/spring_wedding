package com.example.online.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.online.auth.security.CustomUserDetails;
import com.example.online.user.dto.UpdateProfileRequest;
import com.example.online.user.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody UpdateProfileRequest request) {

        userService.updateProfile(user.getId(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile/complete")
    public ResponseEntity<Boolean> isProfileComplete(
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(
                userService.isProfileComplete(user.getId())
        );
    }
}

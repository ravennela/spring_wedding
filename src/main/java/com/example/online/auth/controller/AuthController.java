
package com.example.online.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.online.auth.dto.SendOtpRequestDto;
import com.example.online.auth.dto.SendOtpResponse;
import com.example.online.auth.dto.VerifyOtpRequestDto;
import com.example.online.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(
            @RequestBody SendOtpRequestDto request) {
        SendOtpResponse res = authService.sendOtp(request.getMobileNumber());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestBody VerifyOtpRequestDto request) {
        return ResponseEntity.ok(authService.verifyOtp(request));
    }
}

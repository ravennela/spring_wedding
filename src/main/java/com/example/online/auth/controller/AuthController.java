
package com.example.online.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.online.auth.dto.SendOtpRequestDTO;
import com.example.online.auth.dto.SendOtpResponse;
import com.example.online.auth.dto.VerifyOtpRequestDTO;
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
            @RequestBody SendOtpRequestDTO request) {
       SendOtpResponse res= authService.sendOtp(request.getMobileNumber());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestBody VerifyOtpRequestDTO request) {
        return ResponseEntity.ok(authService.verifyOtp(request));
    }
}

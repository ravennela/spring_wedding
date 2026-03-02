
package com.example.online.auth.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.online.auth.dto.AuthResponseDTO;
import com.example.online.auth.dto.SendOtpResponse;
import com.example.online.auth.dto.VerifyOtpRequestDTO;
import com.example.online.auth.entity.OtpVerification;
import com.example.online.auth.repository.OtpVerificationRepository;
import com.example.online.common.enums.UserRole;
import com.example.online.user.entity.User;
import com.example.online.user.repository.UserRepository;
import com.example.online.utils.JwtUtil;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

    private final OtpVerificationRepository otpRepo;
    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;

    public AuthService(
            OtpVerificationRepository otpRepo,
            UserRepository userRepo,
            JwtUtil jwtUtil) {
        this.otpRepo = otpRepo;
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public SendOtpResponse sendOtp(String phone) {

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        // ✅ delete immediately
        otpRepo.deleteByPhone(phone);

        OtpVerification otpEntity = new OtpVerification();

        otpEntity.setPhone(phone);
        otpEntity.setOtp(otp);
        otpEntity.setVerified(false);
        otpEntity.setExpiresAt(
                LocalDateTime.now().plusMinutes(5));

        otpRepo.saveAndFlush(otpEntity);

        System.out.println("OTP is: " + otp);

        return new SendOtpResponse(
                "OTP sent successfully",
                phone,
                otp);
    }

    @Transactional
    public AuthResponseDTO verifyOtp(VerifyOtpRequestDTO request) {

        OtpVerification otp = otpRepo.findFirstByPhoneOrderByCreatedAtDesc(request.getPhone())
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        System.out.println("otp exp" + otp.getExpiresAt());
        System.out.println("current" + LocalDateTime.now());
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            System.out.println("expired case");
            throw new RuntimeException("OTP expired");
        }
        System.out.println("passed case");
        if (!otp.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        otp.setVerified(true);
        otpRepo.save(otp);

        User user = userRepo.findByPhone(request.getPhone())
                .orElseGet(() -> {
                    User u = new User();
                    u.setPhone(request.getPhone());
                    u.setRole(UserRole.CUSTOMER);
                    return userRepo.save(u);
                });

        String token = jwtUtil.generateToken(
                user.getId().toString(),
                user.getRole().name());

        return new AuthResponseDTO(token, user.getRole().name(), user.getId().toString(), user.getName());
    }
}

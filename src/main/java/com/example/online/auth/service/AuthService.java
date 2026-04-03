
package com.example.online.auth.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.online.auth.dto.AuthResponseDto;
import com.example.online.auth.dto.SendOtpResponse;
import com.example.online.auth.dto.VerifyOtpRequestDto;
import com.example.online.auth.entity.OtpVerification;
import com.example.online.auth.repository.OtpVerificationRepository;
import com.example.online.common.enums.UserRole;
import com.example.online.user.entity.User;
import com.example.online.user.repository.UserRepository;
import com.example.online.auth.util.JwtUtil;
import com.example.online.vendor.entity.Vendor;
import com.example.online.vendor.repository.VendorRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

    private final OtpVerificationRepository otpRepo;
    private final UserRepository userRepo;
    private final VendorRepository vendorRepo;
    private final JwtUtil jwtUtil;
    private final SmsService smsService;

    public AuthService(
            OtpVerificationRepository otpRepo,
            UserRepository userRepo,
            VendorRepository vendorRepo,
            JwtUtil jwtUtil,
            SmsService smsService) {
        this.otpRepo = otpRepo;
        this.userRepo = userRepo;
        this.vendorRepo = vendorRepo;
        this.jwtUtil = jwtUtil;
        this.smsService = smsService;
    }

    @Transactional
    public SendOtpResponse sendOtp(String phone) {
        System.out.println("DEBUG: sendOtp called for phone: " + phone);

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

        // 🚀 Send SMS via Fast2SMS
        String message = "Your OTP is " + otp;
       // smsService.sendSms(phone, message);

        System.out.println("OTP is: " + otp);

        return new SendOtpResponse(
                "OTP sent successfully",
                phone,
                otp);
    }

    @Transactional
    public AuthResponseDto verifyOtp(VerifyOtpRequestDto request) {

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
                    u.setRole(request.getRole() != null ? request.getRole() : UserRole.CUSTOMER);
                    return userRepo.save(u);
                });

        // 🔥 update role if provided
        if (request.getRole() != null && user.getRole() != request.getRole()) {
            user.setRole(request.getRole());
            userRepo.save(user);
        }

        // Vendors need a row in `vendors` (linked by user_id) for dashboard / profile APIs
        ensureVendorRecordExists(user);

        String token = jwtUtil.generateToken(
                user.getId().toString(),
                user.getRole().name());

        return new AuthResponseDto(token, user.getRole().name(), user.getId().toString(), user.getName());
    }

    /**
     * Creates a minimal {@link Vendor} for new VENDOR users. Profile fields can be filled later via
     * {@code PUT /vendor/bookings/profile}.
     */
    private void ensureVendorRecordExists(User user) {
        if (user.getRole() != UserRole.VENDOR) {
            return;
        }
        if (vendorRepo.findByUserId(user.getId()).isPresent()) {
            return;
        }
        Vendor vendor = new Vendor();
        vendor.setUser(user);
        vendorRepo.save(vendor);
    }
}

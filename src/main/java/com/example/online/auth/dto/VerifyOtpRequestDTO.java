package com.example.online.auth.dto;

import com.example.online.common.enums.UserRole;
import com.example.online.user.entity.User;

public class VerifyOtpRequestDto {
    private String phone;
    private String otp;
    private UserRole role;

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}

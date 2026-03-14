package com.example.online.auth.dto;

public class SendOtpResponse {
    private String message;
    private String phone;
    private String otp;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
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
        
    public SendOtpResponse(String message, String phone, String otp) {
        this.message = message;
        this.phone = phone;
        this.otp = otp;
    }
}


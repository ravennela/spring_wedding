package com.example.online.booking.dto;

import jakarta.validation.constraints.NotBlank;

public class AdminCancelRequestDto {
    @NotBlank(message = "Cancel reason is required")
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}







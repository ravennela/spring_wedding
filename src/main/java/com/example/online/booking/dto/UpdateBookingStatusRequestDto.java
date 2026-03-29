package com.example.online.booking.dto;

import com.example.online.common.enums.BookingStatus;

import jakarta.validation.constraints.NotNull;

public class UpdateBookingStatusRequestDto {
   @NotNull(message = "Status is required")
    private BookingStatus status;

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    } 
}






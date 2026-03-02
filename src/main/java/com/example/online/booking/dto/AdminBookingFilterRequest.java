package com.example.online.booking.dto;

import com.example.online.common.enums.BookingStatus;
import com.example.online.common.enums.PaymentStatus;

public class AdminBookingFilterRequest {
    private BookingStatus status;
    private String city;
    private PaymentStatus paymentStatus;
    public BookingStatus getStatus() {
        return status;
    }
    public void setStatus(BookingStatus status) {
        this.status = status;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
}

package com.example.online.booking.dto;

import com.example.online.common.enums.BookingStatus;
import com.example.online.common.enums.PaymentStatus;

public class AdminBookingFilterRequest {
    private BookingStatus status;
    private String city;
    private PaymentStatus paymentStatus;
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;
    private java.math.BigDecimal minAmount;
    private java.math.BigDecimal maxAmount;
    private String customerName;
    private String vendorName;
    private java.util.UUID eventTypeId;

    public java.util.UUID getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(java.util.UUID eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

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

    public java.time.LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(java.time.LocalDate startDate) {
        this.startDate = startDate;
    }

    public java.time.LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(java.time.LocalDate endDate) {
        this.endDate = endDate;
    }

    public java.math.BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(java.math.BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public java.math.BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(java.math.BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}





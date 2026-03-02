package com.example.online.booking.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.example.online.common.enums.PaymentMode;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public class BookingCreateRequestDTO {

    @NotNull
    private UUID eventTypeId;

    @NotNull
    private UUID cityId;

    @NotNull
    private UUID decorationId;
    @NotNull
    private PaymentMode paymentMode;

    @NotNull
    private UUID addressId;

    @NotNull
    @Future
    private LocalDate eventDate;

    private String customerNote;

    public UUID getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(UUID eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public UUID getCityId() {
        return cityId;
    }

    public void setCityId(UUID cityId) {
        this.cityId = cityId;
    }

    public UUID getDecorationId() {
        return decorationId;
    }

    public void setDecorationId(UUID decorationId) {
        this.decorationId = decorationId;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getCustomerNote() {
        return customerNote;
    }

    public void setCustomerNote(String customerNote) {
        this.customerNote = customerNote;
    }

    public UUID getAddressId() {
        return addressId;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    
}

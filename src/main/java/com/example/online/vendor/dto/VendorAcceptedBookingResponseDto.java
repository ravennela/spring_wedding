package com.example.online.vendor.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.example.online.common.enums.BookingStatus;

public class VendorAcceptedBookingResponseDto {
    private UUID bookingId;
    private String eventType;
    private String decoration;
    private LocalDate eventDate;
    private String city;
    private BigDecimal price;
    private BookingStatus status;

    public VendorAcceptedBookingResponseDto(
            UUID bookingId,
            String eventType,
            String decoration,
            LocalDate eventDate,
            String city,
            BigDecimal price,
            BookingStatus status) {

        this.bookingId = bookingId;
        this.eventType = eventType;
        this.decoration = decoration;
        this.eventDate = eventDate;
        this.city = city;
        this.price = price;
        this.status = status;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDecoration() {
        return decoration;
    }

    public void setDecoration(String decoration) {
        this.decoration = decoration;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

}

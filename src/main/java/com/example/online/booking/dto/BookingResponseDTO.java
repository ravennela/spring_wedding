package com.example.online.booking.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.online.common.enums.BookingStatus;

public class BookingResponseDTO {

   private String bookingId;
    private String eventType;
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    private String decorationTitle;
    private String city;
    private LocalDate eventDate;
    private BookingStatus status;
    private double totalAmount;
    private LocalDateTime createdAt;

    // getters & setters

    public String getBookingId() {
        return bookingId;
    }
     public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDecorationTitle() {
        return decorationTitle;
    }

    public void setDecorationTitle(String decorationTitle) {
        this.decorationTitle = decorationTitle;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

   

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}

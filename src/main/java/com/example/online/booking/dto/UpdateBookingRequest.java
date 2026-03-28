package com.example.online.booking.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.example.online.common.enums.BookingStatus;

import java.util.List;

public class UpdateBookingRequest {
    private List<String> vendorIds;
    private String decorationId; // 👈 ADD THIS
    private BigDecimal totalAmount;
    private BigDecimal advanceAmount;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private BookingStatus status;
    private String note;
    
    public List<String> getVendorIds() {
        return vendorIds;
    }
    public void setVendorIds(List<String> vendorIds) {
        this.vendorIds = vendorIds;
    }
    public String getDecorationId() {
        return decorationId;
    }
    public void setDecorationId(String decorationId) {
        this.decorationId = decorationId;
    }
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    public BigDecimal getAdvanceAmount() {
        return advanceAmount;
    }
    public void setAdvanceAmount(BigDecimal advanceAmount) {
        this.advanceAmount = advanceAmount;
    }
    public LocalDate getEventDate() {
        return eventDate;
    }
    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }
    public LocalTime getEventTime() {
        return eventTime;
    }
    public void setEventTime(LocalTime eventTime) {
        this.eventTime = eventTime;
    }
    public BookingStatus getStatus() {
        return status;
    }
    public void setStatus(BookingStatus status) {
        this.status = status;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    
}

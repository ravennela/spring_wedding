package com.example.online.vendor.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.example.online.common.enums.BookingStatus;

public class RecentJobDto {
    private UUID id;
    private String title;
    private String location;
    private LocalDate date;
    private String status;
    private BigDecimal amount;

    public RecentJobDto(UUID id,
            String title,
            String location,
            LocalDate date,
            BookingStatus status,
            BigDecimal amount) {

        this.id = id;
        this.title = title;
        this.location = location;
        this.date = date;
        this.status = status != null ? status.name() : null;
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}

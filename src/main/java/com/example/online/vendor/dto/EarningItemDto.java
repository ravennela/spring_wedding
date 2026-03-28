package com.example.online.vendor.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.example.online.common.enums.PaymentStatus;

public class EarningItemDto {

    private UUID bookingId;
    private String eventName;
    private LocalDate eventDate;
    private BigDecimal amount;
    private String paymentStatus;

    public EarningItemDto(UUID bookingId,
                          String eventName,
                          LocalDate eventDate,
                          BigDecimal amount,
                          PaymentStatus paymentStatus) {

        this.bookingId = bookingId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.amount = amount;
        this.paymentStatus = paymentStatus != null ? paymentStatus.name() : null;
    }

    public UUID getBookingId() { return bookingId; }
    public String getEventName() { return eventName; }
    public LocalDate getEventDate() { return eventDate; }
    public BigDecimal getAmount() { return amount; }
    public String getPaymentStatus() { return paymentStatus; }
}
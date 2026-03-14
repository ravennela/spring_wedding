package com.example.online.payment.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateOrderRequest {

    private UUID bookingId;
    private BigDecimal amount;

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

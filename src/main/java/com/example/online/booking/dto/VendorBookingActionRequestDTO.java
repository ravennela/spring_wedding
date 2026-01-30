package com.example.online.booking.dto;

import java.util.UUID;

public class VendorBookingActionRequestDTO {

    private UUID vendorId;

    public UUID getVendorId() {
        return vendorId;
    }

    public void setVendorId(UUID vendorId) {
        this.vendorId = vendorId;
    }
}

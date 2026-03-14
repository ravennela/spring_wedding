package com.example.online.vendor.dto;

import java.util.UUID;

public class VendorBookingActionRequestDto {

    private UUID vendorId;

    public UUID getVendorId() {
        return vendorId;
    }

    public void setVendorId(UUID vendorId) {
        this.vendorId = vendorId;
    }
}

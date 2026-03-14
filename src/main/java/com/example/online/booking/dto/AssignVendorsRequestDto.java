package com.example.online.booking.dto;

import java.util.List;
import java.util.UUID;

public class AssignVendorsRequestDto {
    private List<UUID> vendorIds;

    public List<UUID> getVendorIds() {
        return vendorIds;
    }

    public void setVendorIds(List<UUID> vendorIds) {
        this.vendorIds = vendorIds;
    }
}






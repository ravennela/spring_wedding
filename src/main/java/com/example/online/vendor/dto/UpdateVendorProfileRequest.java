package com.example.online.vendor.dto;



import java.util.UUID;

public class UpdateVendorProfileRequest {

    private String name;
    private String email;
    private UUID cityId;

    private String companyName;
    private String serviceType;
    private String address;
    private String description;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UUID getCityId() {
        return cityId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }
}

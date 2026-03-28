package com.example.online.vendor.dto;

public class VendorProfileResponseDto {

    private String id;
    private String name;
    private String email;
    private String phone;

    private String companyName;
    private String serviceType;
    private String city;
    private String address;
    private String description;

    public VendorProfileResponseDto(
            String id,
            String name,
            String email,
            String phone,
            String companyName,
            String serviceType,
            String city,
            String address,
            String description) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.companyName = companyName;
        this.serviceType = serviceType;
        this.city = city;
        this.address = address;
        this.description = description;
    }

    // getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }
}
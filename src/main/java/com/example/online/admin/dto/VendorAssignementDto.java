package com.example.online.admin.dto;

import java.util.UUID;

public class VendorAssignementDto {
    private UUID vendorId;
    private String vendorName;
    private String companyName;
    private String phone;
    private boolean assigned;
    private String serviceType;
    private String city;
    private String address;
    private String description;
    private boolean active;

    public VendorAssignementDto(UUID vendorId, String vendorName, String companyName, String phone, boolean assigned, 
                               String serviceType, String city, String address, String description, boolean active) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.companyName = companyName;
        this.phone = phone;
        this.assigned = assigned;
        this.serviceType = serviceType;
        this.city = city;
        this.address = address;
        this.description = description;
        this.active = active;
    }

    public VendorAssignementDto() {
    }

    public UUID getVendorId() {
        return vendorId;
    }
    public void setVendorId(UUID vendorId) {
        this.vendorId = vendorId;
    }
    public String getVendorName() {
        return vendorName;
    }
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public boolean isAssigned() {
        return assigned;
    }
    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
    public String getServiceType() {
        return serviceType;
    }
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
}

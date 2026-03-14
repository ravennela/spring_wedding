
package com.example.online.vendor.entity;

import com.example.online.common.entity.BaseEntity;
import com.example.online.common.enums.ServiceType;
import com.example.online.common.enums.VendorStatus;
import com.example.online.location.entity.City;
import com.example.online.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "vendors")
public class Vendor extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String companyName;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType=ServiceType.DECORATION;

    @Enumerated(EnumType.STRING)
    private VendorStatus status = VendorStatus.ACTIVE;

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    @ManyToOne
    private City city;
    @Column(name = "is_active")
    private boolean isActive = true;

    private String address;

    private String description;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
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

    public VendorStatus getStatus() {
        return status;
    }

    public void setStatus(VendorStatus status) {
        this.status = status;
    }
}

package com.example.online.user.entity;

import com.example.online.common.entity.BaseEntity;
import com.example.online.common.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(nullable = true)
    private String name;

    @Column(unique = true, nullable = false)
    private String phone;

    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean isActive = true;

    @Column(nullable = false)
    private boolean isVerified = false; 
}

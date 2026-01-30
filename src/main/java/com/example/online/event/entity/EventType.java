package com.example.online.event.entity;


import com.example.online.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "event_types")
public class EventType extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
    private boolean isActive = true;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    
}

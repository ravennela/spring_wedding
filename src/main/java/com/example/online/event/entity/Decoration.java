package com.example.online.event.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.online.common.entity.BaseEntity;
import com.example.online.location.enitity.City;
import com.example.online.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "decorations")
public class Decoration extends BaseEntity {

    @ManyToOne
    private EventType eventType;

    @ManyToOne
    private City city;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String inclusions;

    @Column(columnDefinition = "TEXT")
    private String exclusions;

    private double basePrice;

    private boolean isActive = true;

    @ManyToOne
    private User createdByAdmin;

    @OneToMany(
        mappedBy = "decoration",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<DecorationImage> images = new ArrayList<>();

    // -------- Getters & Setters --------

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInclusions() {
        return inclusions;
    }

    public void setInclusions(String inclusions) {
        this.inclusions = inclusions;
    }

    public String getExclusions() {
        return exclusions;
    }

    public void setExclusions(String exclusions) {
        this.exclusions = exclusions;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public User getCreatedByAdmin() {
        return createdByAdmin;
    }

    public void setCreatedByAdmin(User createdByAdmin) {
        this.createdByAdmin = createdByAdmin;
    }

    public List<DecorationImage> getImages() {
        return images;
    }

    public void setImages(List<DecorationImage> images) {
        this.images = images;
    }
}

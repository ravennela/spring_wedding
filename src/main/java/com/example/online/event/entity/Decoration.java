package com.example.online.event.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.online.common.entity.BaseEntity;
import com.example.online.location.entity.City;
import com.example.online.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "decorations")
public class Decoration extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_type_id")
    private EventType eventType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id")
    private City city;
    

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String inclusions;

    @Column(columnDefinition = "TEXT")
    private String exclusions;

    @Column(nullable = false)
    private BigDecimal basePrice;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "created_by_admin_id")
    private User createdByAdmin;

    @OneToMany(
        mappedBy = "decoration",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<DecorationImage> images = new ArrayList<>();

    // getters & setters

    public void addImage(DecorationImage image) {
        images.add(image);
        image.setDecoration(this);
    }

    public void removeImage(DecorationImage image) {
        images.remove(image);
        image.setDecoration(null);
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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


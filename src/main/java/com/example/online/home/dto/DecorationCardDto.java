package com.example.online.home.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.example.online.event.entity.DecorationImage;

public class DecorationCardDto {
    private UUID id;
    private String name;
    private BigDecimal price;
    private List<String> imageUrl;

    public DecorationCardDto(UUID id, String name, BigDecimal price, List<String> imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String > imageUrl) {
        this.imageUrl = imageUrl;
    }

}

package com.example.online.home.dto;

import java.util.List;

public class HomeResponse {
    private HeroSectionDto hero;
    private List<CategoryItemDto> categories;
    private List<ServiceItemDto> services;
    private List<FeaturedEventDto> featuredEvent;
    private List<CelebrationItemDto> realCelebrations;
    private List<DecorationCardDto> trendingDecorations;

    public HomeResponse(HeroSectionDto hero, List<CategoryItemDto> categories, List<ServiceItemDto> services,
            List<FeaturedEventDto> featuredEvent, List<CelebrationItemDto> realCelebrations,
            List<DecorationCardDto> trendingDecorations) {
        this.hero = hero;
        this.categories = categories;
        this.services = services;
        this.featuredEvent = featuredEvent;
        this.realCelebrations = realCelebrations;
        this.trendingDecorations = trendingDecorations;
    }

    public HeroSectionDto getHero() {
        return hero;
    }

    public void setHero(HeroSectionDto hero) {
        this.hero = hero;
    }

    public List<CategoryItemDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryItemDto> categories) {
        this.categories = categories;
    }

    public List<ServiceItemDto> getServices() {
        return services;
    }

    public void setServices(List<ServiceItemDto> services) {
        this.services = services;
    }

    public List<FeaturedEventDto> getFeaturedEvent() {
        return featuredEvent;
    }

    public void setFeaturedEvent(List<FeaturedEventDto> featuredEvent) {
        this.featuredEvent = featuredEvent;
    }

    public List<CelebrationItemDto> getRealCelebrations() {
        return realCelebrations;
    }

    public void setRealCelebrations(List<CelebrationItemDto> realCelebrations) {
        this.realCelebrations = realCelebrations;
    }

    public List<DecorationCardDto> getTrendingDecorations() {
        return trendingDecorations;
    }

    public void setTrendingDecorations(List<DecorationCardDto> trendingDecorations) {
        this.trendingDecorations = trendingDecorations;
    }

}

package com.example.online.home.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.online.event.entity.Decoration;
import com.example.online.event.entity.EventType;
import com.example.online.event.repository.DecorationRepository;
import com.example.online.home.dto.CategoryItemDto;
import com.example.online.event.repository.EventTypeRepository;
import com.example.online.home.dto.CelebrationItemDto;
import com.example.online.home.dto.DecorationCardDto;
import com.example.online.home.dto.FeaturedEventDto;
import com.example.online.home.dto.HeroSectionDto;
import com.example.online.home.dto.HomeResponse;
import com.example.online.home.dto.ServiceItemDto;
import java.util.List;

import java.util.stream.Collectors;

@Service
public class HomeServiceImpl implements HomeService {
        @Autowired
        private EventTypeRepository eventTypeRepository;
        @Autowired
        private DecorationRepository decorationRepository;

        @Override
        public HomeResponse getHomeData() {

                // 1. Fetch categories (event types)
                List<EventType> eventTypes = eventTypeRepository.findByIsActiveTrue();

                List<CategoryItemDto> categories = eventTypes.stream()
                                .map(event -> new CategoryItemDto(
                                                event.getId(),
                                                event.getName(),
                                                "default_icon",
                                                event.getIconUrl()))
                                .collect(Collectors.toList());

                // 2. Fetch trending decorations
                List<Decoration> decorations = decorationRepository.findTop5ByActiveTrueOrderByCreatedAtDesc();

                List<DecorationCardDto> trendingDecorations = decorations.stream()
                                .map(dec -> new DecorationCardDto(
                                                dec.getId(),
                                                dec.getName(),
                                                dec.getBasePrice(),
                                                null))
                                .collect(Collectors.toList());

                // 3. Static services section
                List<ServiceItemDto> services = List.of(
                                new ServiceItemDto(
                                                "Custom Decor",
                                                "Tailored designs that match your vision.",
                                                "decor"),
                                new ServiceItemDto(
                                                "Photography",
                                                "Capturing every moment with excellence.",
                                                "camera"),
                                new ServiceItemDto(
                                                "Entertainment",
                                                "Live bands, DJs, and performances.",
                                                "music"),
                                new ServiceItemDto(
                                                "Catering",
                                                "Exquisite menus crafted by top chefs.",
                                                "food"));

                // 4. Hero section (static for now)
                HeroSectionDto hero = new HeroSectionDto(
                                "Elegance in Every Detail",
                                "Create Timeless Memories",
                                null);

                // 5. Featured event (static for now)
                List<FeaturedEventDto> featuredEvent = List.of(new FeaturedEventDto(
                                null,
                                "The Royal Wedding",
                                "Elegant Palaces & Premium Decor",
                                null),
                                new FeaturedEventDto(
                                                null,
                                                "Birthday",
                                                "Premium decorations for Birthday",
                                                null));

                // 6. Real celebrations (static for now)
                List<CelebrationItemDto> celebrations = List.of(
                                new CelebrationItemDto(
                                                "Suhas & Priya",
                                                "Wedding",
                                                null),
                                new CelebrationItemDto(
                                                "Corporate Annual Meet",
                                                "Corporate",
                                                null));

                // 7. Build final response
                return new HomeResponse(
                                hero,
                                categories,
                                services,
                                featuredEvent,
                                celebrations,
                                trendingDecorations);
        }

}

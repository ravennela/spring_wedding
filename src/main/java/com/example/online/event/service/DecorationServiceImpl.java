package com.example.online.event.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.online.catalog.dto.DecorationResponseDto;
import com.example.online.common.enums.UserRole;
import com.example.online.event.dto.CreateDecorationRequest;
import com.example.online.event.dto.DecorationImageRequest;
import com.example.online.event.dto.DecorationImageResponse;
import com.example.online.event.dto.DecorationResponse;
import com.example.online.event.dto.UpdateDecorationRequest;
import com.example.online.event.entity.Decoration;
import com.example.online.event.entity.DecorationImage;
import com.example.online.event.entity.EventType;
import com.example.online.event.repository.DecorationImageRepository;
import com.example.online.event.repository.DecorationRepository;
import com.example.online.event.repository.EventTypeRepository;
import com.example.online.location.entity.City;
import com.example.online.location.repository.CityRepository;
import com.example.online.user.entity.User;
import com.example.online.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DecorationServiceImpl implements DecorationService {
    @Autowired
    private DecorationRepository decorationRepository;
    @Autowired
    private DecorationImageRepository decorationImageRepository;
    @Autowired
    private EventTypeRepository eventTypeRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public DecorationResponseDto createDecoration(CreateDecorationRequest request) {
        EventType eventType = eventTypeRepository.findById(request.getEventTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Event type not found"));
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("City not found"));
        User admin = userRepository.findFirstByRole(UserRole.ADMIN)
                .orElseThrow(() -> new IllegalStateException("Admin user not found"));

        Decoration decoration = new Decoration();
        decoration.setEventType(eventType);
        decoration.setCity(city);
        decoration.setName(request.getName());
        decoration.setDescription(request.getDescription());
        decoration.setInclusions(request.getInclusions());
        decoration.setExclusions(request.getExclusions());
        decoration.setBasePrice(request.getBasePrice());
        if (request.getActive() != null) {
            decoration.setActive(request.getActive());
        }
        decoration.setCreatedByAdmin(admin);

        attachImagesFromCreateRequest(decoration, request);

        Decoration saved = decorationRepository.save(decoration);

        DecorationResponseDto responseDto = new DecorationResponseDto();
        responseDto.setId(saved.getId());
        responseDto.setTitle(saved.getName());
        responseDto.setDescription(saved.getDescription());
        responseDto.setBasePrice(saved.getBasePrice());
        responseDto.setInclusions(saved.getInclusions());
        responseDto.setExclusions(saved.getExclusions());
        responseDto.setImageUrls(saved.getImages().stream()
                .map(DecorationImage::getImageUrl)
                .collect(Collectors.toList()));
        return responseDto;
    }

    private void attachImagesFromCreateRequest(Decoration decoration, CreateDecorationRequest request) {
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (DecorationImageRequest ir : request.getImages()) {
                addImageIfValid(decoration, ir.getImageUrl(), ir.getPublicId());
            }
            return;
        }
        if (request.getImageUrls() != null) {
            for (String url : request.getImageUrls()) {
                addImageIfValid(decoration, url, null);
            }
        }
    }

    private void addImageIfValid(Decoration decoration, String url, String publicId) {
        if (url == null || url.isBlank()) {
            return;
        }
        DecorationImage image = new DecorationImage();
        image.setImageUrl(url.trim());
        if (publicId != null && !publicId.isBlank()) {
            image.setPublicId(publicId.trim());
        }
        decoration.addImage(image);
    }

    private DecorationResponse toDecorationResponse(Decoration d, List<DecorationImage> orderedImages) {
        List<DecorationImageResponse> imgResp = orderedImages.stream()
                .map(img -> new DecorationImageResponse(img.getId(), img.getImageUrl(), img.getPublicId()))
                .collect(Collectors.toList());
        List<String> urls = orderedImages.stream()
                .map(DecorationImage::getImageUrl)
                .collect(Collectors.toList());
        return new DecorationResponse(
                d.getId(),
                d.getName(),
                d.getDescription(),
                d.getInclusions(),
                d.getExclusions(),
                d.getBasePrice(),
                d.isActive(),
                d.getCity().getId(),
                d.getCity().getName(),
                d.getEventType().getId(),
                d.getEventType().getName(),
                imgResp,
                urls);
    }

    @Override
    public Page<DecorationResponse> getDecorations(String search, UUID cityId, UUID eventTypeId, Boolean active,
            org.springframework.data.domain.Pageable pageable) {
        Page<Decoration> page = decorationRepository.findDecorations(search, cityId, eventTypeId, active, pageable);
        List<UUID> ids = page.getContent().stream().map(Decoration::getId).collect(Collectors.toList());
        Map<UUID, List<DecorationImage>> byDecoration = new LinkedHashMap<>();
        if (!ids.isEmpty()) {
            for (DecorationImage di : decorationImageRepository.findAllByDecorationIds(ids)) {
                UUID decId = di.getDecoration().getId();
                byDecoration.computeIfAbsent(decId, k -> new ArrayList<>()).add(di);
            }
        }
        return page.map(d -> toDecorationResponse(d, byDecoration.getOrDefault(d.getId(), List.of())));
    }

    @Override
    public DecorationResponse updateDecoration(UUID id, UpdateDecorationRequest request) {
        Decoration decoration = decorationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Decoration not found"));

        decoration.setName(request.getName());
        decoration.setDescription(request.getDescription());
        decoration.setInclusions(request.getInclusions());
        decoration.setExclusions(request.getExclusions());
        decoration.setBasePrice(request.getBasePrice());

        if (request.getCityId() != null) {
            City city = cityRepository.findById(request.getCityId())
                    .orElseThrow(() -> new IllegalArgumentException("City not found"));
            decoration.setCity(city);
        }
        if (request.getEventTypeId() != null) {
            EventType eventType = eventTypeRepository.findById(request.getEventTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("Event type not found"));
            decoration.setEventType(eventType);
        }
        decoration.setActive(request.isActive());

        if (request.getImages() != null) {
            decoration.clearImages();
            for (DecorationImageRequest ir : request.getImages()) {
                addImageIfValid(decoration, ir.getImageUrl(), ir.getPublicId());
            }
        }

        Decoration updated = decorationRepository.save(decoration);
        List<DecorationImage> imgs = new ArrayList<>(updated.getImages());
        return toDecorationResponse(updated, imgs);
    }

    @Override
    public void deleteDecoration(UUID id) {
        Decoration decoration = decorationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Decoration not found"));
        decoration.setActive(false);
        decorationRepository.save(decoration);
    }

    @Override
    public DecorationResponse getDecorationById(UUID id) {
        Decoration decoration = decorationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Decoration not found"));
        List<DecorationImage> imgs = new ArrayList<>(decoration.getImages());
        imgs.sort((a, b) -> {
            if (a.getCreatedAt() == null || b.getCreatedAt() == null) {
                return 0;
            }
            return a.getCreatedAt().compareTo(b.getCreatedAt());
        });
        return toDecorationResponse(decoration, imgs);
    }
}

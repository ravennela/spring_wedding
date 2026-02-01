package com.example.online.event.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.domain.Page;

import com.example.online.catlog.dto.DecorationResponseDTO;
import com.example.online.common.enums.UserRole;
import com.example.online.event.dto.CreateDecorationRequest;
import com.example.online.event.dto.DecorationResponse;
import com.example.online.event.entity.Decoration;
import com.example.online.event.entity.DecorationImage;
import com.example.online.event.entity.EventType;
import com.example.online.event.repository.DecorationRepository;
import com.example.online.event.repository.EventTypeRepository;
import com.example.online.location.enitity.City;
import com.example.online.location.repository.CityRepository;
import com.example.online.user.entity.User;
import com.example.online.user.repository.UserRepository;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
@Service
@Transactional
public class DecorationServiceImpl implements DecorationService {
    @Autowired
    private DecorationRepository decorationRepository;
    @Autowired
    private  EventTypeRepository eventTypeRepository;
    @Autowired
    private  CityRepository cityRepository;
    @Autowired
    private  UserRepository userRepository;

    @Override
    public DecorationResponseDTO createDecoration(CreateDecorationRequest request) {

        // 1️⃣ Fetch EventType
        EventType eventType = eventTypeRepository.findById(
                request.getEventTypeId()).orElseThrow(() -> new IllegalArgumentException("Event type not found"));

        // 2️⃣ Fetch City
        City city = cityRepository.findById(
                request.getCityId()).orElseThrow(() -> new IllegalArgumentException("City not found"));

        // 3️⃣ Get Admin (TEMP: first admin, later JWT)
        User admin = userRepository.findFirstByRole(UserRole.ADMIN)
        .orElseThrow(() -> new IllegalStateException("Admin user not found"));

        // 4️⃣ Build Decoration
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

        // 5️⃣ Map Images
        if (request.getImageUrls() != null) {
            request.getImageUrls().forEach(url -> {
                DecorationImage image = new DecorationImage();
                image.setImageUrl(url);
                decoration.addImage(image);
            });
        }

        // 6️⃣ Save
        Decoration saved = decorationRepository.save(decoration);

        DecorationResponseDTO responseDTO = new DecorationResponseDTO();
        responseDTO.setId(saved.getId());
        responseDTO.setTitle(saved.getName());
        responseDTO.setDescription(saved.getDescription());
        responseDTO.setBasePrice(saved.getBasePrice());
        responseDTO.setInclusions(saved.getInclusions());
    
        return responseDTO;
    }

    @Override
    public Page<DecorationResponse> getDecorations(String search, UUID cityId, UUID eventTypeId, Boolean active, org.springframework.data.domain.Pageable pageable) {
        Page<Decoration> page = decorationRepository.findDecorations(
                search,
                cityId,
                eventTypeId,
                active,
                pageable
        );
        return page.map(d -> new DecorationResponse(
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
                d.getEventType().getName()
        ));
    }
}
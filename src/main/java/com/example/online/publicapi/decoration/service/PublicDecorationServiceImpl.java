package com.example.online.publicapi.decoration.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.online.event.entity.Decoration;
import com.example.online.event.entity.DecorationImage;
import com.example.online.event.repository.DecorationImageRepository;
import com.example.online.event.repository.DecorationRepository;
import com.example.online.publicapi.decoration.dto.DecorationDetailDto;
import com.example.online.publicapi.decoration.dto.DecorationListItemDto;

@Service
public class PublicDecorationServiceImpl implements PublicDecorationService {

        @Autowired
        private DecorationRepository decorationRepository;
        @Autowired
        private DecorationImageRepository decorationImageRepository;

        @Override
        @Transactional(readOnly = true)
        public DecorationDetailDto getDecorationDetail(UUID decorationId) {

                Decoration dec = decorationRepository.findById(decorationId)
                                .filter(Decoration::isActive)
                                .orElseThrow(() -> new RuntimeException("Decoration not found"));

                List<String> imageUrls = dec.getImages().stream()
                                .sorted((a, b) -> {
                                        if (a.getCreatedAt() == null || b.getCreatedAt() == null) {
                                                return 0;
                                        }
                                        return a.getCreatedAt().compareTo(b.getCreatedAt());
                                })
                                .map(DecorationImage::getImageUrl)
                                .collect(Collectors.toList());

                return new DecorationDetailDto(
                                dec.getId(),
                                dec.getName(),
                                dec.getDescription(),
                                dec.getBasePrice(),
                                imageUrls,
                                dec.getEventType() != null ? dec.getEventType().getName() : null,
                                dec.getCity() != null ? dec.getCity().getName() : null,
                                0, 0, dec.getInclusions(), dec.getExclusions(),
                                dec.getEventType() != null ? dec.getEventType().getId() : null,
                                dec.getCity() != null ? dec.getCity().getId() : null);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<DecorationListItemDto> getDecorations(UUID eventTypeId, UUID cityId, Pageable pageable) {
                Page<Decoration> decorationPage = decorationRepository.findDecorations(
                                null,
                                cityId,
                                eventTypeId,
                                true,
                                pageable);

                List<UUID> ids = decorationPage.getContent().stream()
                                .map(Decoration::getId)
                                .collect(Collectors.toList());
                Map<UUID, String> thumbnailByDecoration = new HashMap<>();
                if (!ids.isEmpty()) {
                        for (DecorationImage di : decorationImageRepository.findAllByDecorationIds(ids)) {
                                UUID did = di.getDecoration().getId();
                                thumbnailByDecoration.putIfAbsent(did, di.getImageUrl());
                        }
                }

                return decorationPage.map(dec -> new DecorationListItemDto(
                                dec.getId(),
                                dec.getName(),
                                dec.getBasePrice(),
                                thumbnailByDecoration.get(dec.getId()),
                                dec.getEventType() != null ? dec.getEventType().getName() : null,
                                dec.getCity() != null ? dec.getCity().getName() : null));

        }
}

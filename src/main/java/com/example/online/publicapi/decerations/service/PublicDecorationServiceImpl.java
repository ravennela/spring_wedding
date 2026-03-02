package com.example.online.publicapi.events.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.online.event.entity.Decoration;
import com.example.online.event.repository.DecorationRepository;
import com.example.online.publicapi.events.dto.DecorationDetailDto;
import com.example.online.publicapi.events.dto.DecorationListItemDto;

@Service
public class PublicDecorationServiceImpl implements PublicDecorationService {

        @Autowired
        private DecorationRepository decorationRepository;

        @Override
        public DecorationDetailDto getDecorationDetail(UUID decorationId) {

                Decoration dec = decorationRepository.findById(decorationId)
                                .filter(Decoration::isActive)
                                .orElseThrow(() -> new RuntimeException("Decoration not found"));

                List<String> imageUrls = Collections.emptyList();

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
        public Page<DecorationListItemDto> getDecorations(UUID eventTypeId, UUID cityId, Pageable pageable) {
                Page<Decoration> decorationPage = decorationRepository.findDecorations(
                                null, // search
                                cityId,
                                eventTypeId,
                                true, // only active decorations for users
                                pageable);

                return decorationPage.map(dec -> new DecorationListItemDto(
                                dec.getId(),
                                dec.getName(),
                                dec.getBasePrice(),
                                null,
                                dec.getEventType() != null ? dec.getEventType().getName() : null,
                                dec.getCity() != null ? dec.getCity().getName() : null));

        }
}

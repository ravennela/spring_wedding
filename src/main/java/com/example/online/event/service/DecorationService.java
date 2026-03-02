package com.example.online.event.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.online.catlog.dto.DecorationResponseDTO;
import com.example.online.event.dto.CreateDecorationRequest;
import com.example.online.event.dto.DecorationResponse;
import com.example.online.event.dto.UpdateDecorationRequest;

public interface DecorationService {

    DecorationResponseDTO createDecoration(CreateDecorationRequest request);

    Page<DecorationResponse> getDecorations(
            String search,
            UUID cityId,
            UUID eventTypeId,
            Boolean active,
            Pageable pageable
    );
    DecorationResponse updateDecoration(UUID id, UpdateDecorationRequest request);

    void deleteDecoration(UUID id);

    DecorationResponse getDecorationById(UUID id);
}

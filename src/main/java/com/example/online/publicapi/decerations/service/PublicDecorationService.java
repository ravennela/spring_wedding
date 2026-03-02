package com.example.online.publicapi.events.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.online.publicapi.events.dto.DecorationDetailDto;
import com.example.online.publicapi.events.dto.DecorationListItemDto;

public interface PublicDecorationService {
    DecorationDetailDto getDecorationDetail(UUID decorationId);
     Page<DecorationListItemDto> getDecorations(
            UUID eventTypeId,
            UUID cityId,
            Pageable pageable
    );
    
}
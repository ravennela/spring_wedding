package com.example.online.publicapi.decoration.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.online.publicapi.decoration.dto.DecorationDetailDto;
import com.example.online.publicapi.decoration.dto.DecorationListItemDto;

public interface PublicDecorationService {
    DecorationDetailDto getDecorationDetail(UUID decorationId);

    Page<DecorationListItemDto> getDecorations(
            UUID eventTypeId,
            UUID cityId,
            Pageable pageable);

}

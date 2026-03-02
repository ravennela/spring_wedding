package com.example.online.publicapi.events.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.online.publicapi.events.dto.DecorationDetailDto;
import com.example.online.publicapi.events.dto.DecorationListItemDto;
import com.example.online.publicapi.events.service.PublicDecorationService;

@RestController
@RequestMapping("/api/public/decorations")
public class PublicDecorationController {

    private final PublicDecorationService decorationService;

    public PublicDecorationController(PublicDecorationService decorationService) {
        this.decorationService = decorationService;
    }
    

    @GetMapping
    public Page<DecorationListItemDto> getDecorations(
            @RequestParam(required = false) UUID eventTypeId,
            @RequestParam(required = false) UUID cityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return decorationService.getDecorations(eventTypeId, cityId, pageable);
    }

    @GetMapping("/{id}")
    public DecorationDetailDto getDecorationDetail(
            @PathVariable UUID id) {

        return decorationService.getDecorationDetail(id);
    }
}

package com.example.online.event.controller;

import com.example.online.event.service.DecorationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.example.online.catlog.dto.DecorationResponseDTO;
import com.example.online.event.dto.CreateDecorationRequest;

import com.example.online.event.dto.PagedResponse;
import java.util.UUID;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.online.event.dto.DecorationResponse;

@RestController
@RequestMapping("/api/admin/decorations")
public class DecorationController {
        @Autowired
        private DecorationService decorationService;

        // ✅ CREATE DECORATION
        @PostMapping
        public ResponseEntity<?> createDecoration(
                        @RequestBody CreateDecorationRequest request) {
                DecorationResponseDTO response = decorationService.createDecoration(request);

                return ResponseEntity.ok(response);
        }

        @GetMapping
        public PagedResponse<DecorationResponse> getDecorations(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(required = false) String search,
                        @RequestParam(required = false) UUID cityId,
                        @RequestParam(required = false) UUID eventTypeId,
                        @RequestParam(required = false) Boolean active,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "desc") String sortDir) {

                Sort sort = sortDir.equalsIgnoreCase("desc")
                                ? Sort.by(sortBy).descending()
                                : Sort.by(sortBy).ascending();

                Pageable pageable = PageRequest.of(page, size, sort);

                Page<DecorationResponse> result = decorationService.getDecorations(
                                search,
                                cityId,
                                eventTypeId,
                                active,
                                pageable);

                return new PagedResponse<>(
                                result.getContent(),
                                result.isLast(),
                                result.getNumber(),
                                result.getSize(),
                                result.getTotalElements(),
                                result.getTotalPages()

                );
        }

        @PutMapping("/{id}")
        public ResponseEntity<?> updateDecoration(
                        @PathVariable UUID id,
                        @RequestBody com.example.online.event.dto.UpdateDecorationRequest request) {

                DecorationResponse response = decorationService.updateDecoration(id, request);
                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteDecoration(@PathVariable UUID id) {
                decorationService.deleteDecoration(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/{id}")
        public ResponseEntity<?> getDecorationById(@PathVariable UUID id) {
                DecorationResponse response = decorationService.getDecorationById(id);
                return ResponseEntity.ok(response);
        }
}

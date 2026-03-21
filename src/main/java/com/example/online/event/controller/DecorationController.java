package com.example.online.event.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.online.catalog.dto.DecorationResponseDto;
import com.example.online.event.dto.CreateDecorationRequest;
import com.example.online.event.dto.DecorationImageDto;
import com.example.online.event.dto.DecorationResponse;
import com.example.online.event.dto.PagedResponse;
import com.example.online.event.service.DecorationImageService;
import com.example.online.event.service.DecorationService;

@RestController
@RequestMapping("/api/admin/decorations")
public class DecorationController {
        @Autowired
        private DecorationService decorationService;
        @Autowired
        private DecorationImageService decorationImageService;

        // ✅ CREATE DECORATION
        @PostMapping
        public ResponseEntity<?> createDecoration(
                        @RequestBody CreateDecorationRequest request) {
                DecorationResponseDto response = decorationService.createDecoration(request);

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

        // ---------- Decoration images (multiple images, separate decoration_images table) ----------

        /**
         * Upload one or more images for a decoration.
         * Accepts multipart form with parameter "files" (multiple files allowed).
         */
        @PostMapping("/{decorationId}/images")
        public ResponseEntity<List<DecorationImageDto>> uploadImages(
                        @PathVariable UUID decorationId,
                        @RequestParam("files") List<MultipartFile> files) {
                List<DecorationImageDto> uploaded = decorationImageService.addImages(decorationId, files);
                return ResponseEntity.ok(uploaded);
        }

        /**
         * List all images for a decoration.
         */
        @GetMapping("/{decorationId}/images")
        public ResponseEntity<List<DecorationImageDto>> getDecorationImages(@PathVariable UUID decorationId) {
                List<DecorationImageDto> images = decorationImageService.getImagesByDecorationId(decorationId);
                return ResponseEntity.ok(images);
        }

        /**
         * Delete a single image from a decoration by image id.
         */
        @DeleteMapping("/{decorationId}/images/{imageId}")
        public ResponseEntity<Void> deleteDecorationImage(
                        @PathVariable UUID decorationId,
                        @PathVariable UUID imageId) {
                decorationImageService.deleteImage(decorationId, imageId);
                return ResponseEntity.noContent().build();
        }
}

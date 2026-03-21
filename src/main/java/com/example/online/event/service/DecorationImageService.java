package com.example.online.event.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.example.online.event.dto.DecorationImageDto;

public interface DecorationImageService {

    /**
     * Upload one or more images for a decoration. Stores files (e.g. via Cloudinary)
     * and creates decoration_images rows linked to the decoration.
     */
    List<DecorationImageDto> addImages(UUID decorationId, List<MultipartFile> files);

    /**
     * Remove a single image from a decoration by image id.
     * Validates that the image belongs to the given decoration.
     */
    void deleteImage(UUID decorationId, UUID imageId);

    /**
     * List all images for a decoration.
     */
    List<DecorationImageDto> getImagesByDecorationId(UUID decorationId);
}

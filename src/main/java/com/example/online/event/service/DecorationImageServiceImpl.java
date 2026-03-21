package com.example.online.event.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.online.event.dto.DecorationImageDto;
import com.example.online.event.entity.Decoration;
import com.example.online.event.entity.DecorationImage;
import com.example.online.event.repository.DecorationImageRepository;
import com.example.online.event.repository.DecorationRepository;
import com.example.online.utils.FileUploadService;
import com.example.online.utils.UploadResult;

@Service
public class DecorationImageServiceImpl implements DecorationImageService {

    private static final String UPLOAD_FOLDER = "decorations";

    @Autowired
    private DecorationRepository decorationRepository;
    @Autowired
    private DecorationImageRepository decorationImageRepository;
    @Autowired
    private FileUploadService fileUploadService;

    @Override
    @Transactional
    public List<DecorationImageDto> addImages(UUID decorationId, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return List.of();
        }
        Decoration decoration = decorationRepository.findById(decorationId)
                .orElseThrow(() -> new IllegalArgumentException("Decoration not found"));
        List<DecorationImageDto> result = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }
            UploadResult uploadResult;
            try {
                uploadResult = fileUploadService.uploadFile(file, UPLOAD_FOLDER);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image: " + (file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown"), e);
            }
            DecorationImage image = new DecorationImage();
            image.setImageUrl(uploadResult.getUrl());
            if (uploadResult.getPublicId() != null) {
                image.setPublicId(uploadResult.getPublicId());
            }
            decoration.addImage(image);
            DecorationImage saved = decorationImageRepository.save(image);
            result.add(new DecorationImageDto(saved.getId(), saved.getImageUrl()));
        }
        return result;
    }

    @Override
    @Transactional
    public void deleteImage(UUID decorationId, UUID imageId) {
        Decoration decoration = decorationRepository.findById(decorationId)
                .orElseThrow(() -> new IllegalArgumentException("Decoration not found"));
        DecorationImage image = decorationImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Decoration image not found"));
        if (!decoration.getId().equals(image.getDecoration().getId())) {
            throw new IllegalArgumentException("Image does not belong to this decoration");
        }
        decoration.removeImage(image);
        decorationImageRepository.delete(image);
    }

    @Override
    public List<DecorationImageDto> getImagesByDecorationId(UUID decorationId) {
        List<DecorationImage> images = decorationImageRepository.findByDecoration_Id(decorationId);
        return images.stream()
                .map(img -> new DecorationImageDto(img.getId(), img.getImageUrl()))
                .toList();
    }
}

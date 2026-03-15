package com.example.online.event.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online.event.entity.DecorationImage;

public interface DecorationImageRepository extends JpaRepository<DecorationImage, Long> {
     List<DecorationImage> findByDecorationId(UUID decorationId);
}

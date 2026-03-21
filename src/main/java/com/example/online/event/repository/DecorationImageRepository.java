package com.example.online.event.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.online.event.entity.DecorationImage;

public interface DecorationImageRepository extends JpaRepository<DecorationImage, UUID> {
    List<DecorationImage> findByDecoration_Id(UUID decorationId);

    @Query("SELECT di FROM DecorationImage di JOIN FETCH di.decoration d WHERE d.id IN :ids ORDER BY d.id, di.createdAt ASC")
    List<DecorationImage> findAllByDecorationIds(@Param("ids") List<UUID> ids);
}

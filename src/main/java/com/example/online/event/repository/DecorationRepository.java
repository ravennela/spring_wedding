package com.example.online.event.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.online.event.entity.Decoration;

public interface DecorationRepository extends JpaRepository<Decoration, UUID> {

    Page<Decoration> findByCityIdAndActiveTrue(
            UUID cityId,
            Pageable pageable
    );
 

    Page<Decoration> findByEventTypeIdAndCityIdAndActiveTrue(
            UUID eventTypeId,
            UUID cityId,
            Pageable pageable
    );

     @Query("""
        SELECT d FROM Decoration d
        WHERE (:search IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:cityId IS NULL OR d.city.id = :cityId)
        AND (:eventTypeId IS NULL OR d.eventType.id = :eventTypeId)
        AND (:active IS NULL OR d.active = :active)
    """)
    Page<Decoration> findDecorations(
            @Param("search") String search,
            @Param("cityId") UUID cityId,
            @Param("eventTypeId") UUID eventTypeId,
            @Param("active") Boolean active,
            Pageable pageable
    );

    Optional<Decoration> findById(UUID id);
     List<Decoration> findTop5ByActiveTrueOrderByCreatedAtDesc();
}

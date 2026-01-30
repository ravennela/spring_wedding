
package com.example.online.event.repository;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online.event.entity.Decoration;

public interface DecorationRepository extends JpaRepository<Decoration, UUID> {

    List<Decoration> findByCityIdAndIsActiveTrue(UUID cityId);

    List<Decoration> findByEventTypeIdAndCityIdAndIsActiveTrue(
        UUID eventTypeId,
        UUID cityId
    );
}


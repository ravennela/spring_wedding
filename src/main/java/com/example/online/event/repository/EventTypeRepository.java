package com.example.online.event.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.online.booking.entity.Booking;
import com.example.online.event.entity.EventType;

public interface EventTypeRepository extends JpaRepository<EventType, UUID> {

  List<EventType> findByIsActive(boolean active);

  boolean existsByNameIgnoreCase(String name);

  Optional<EventType> findByNameIgnoreCase(String name);

  Page<EventType> findByNameContainingIgnoreCase(
      String name,
      Pageable pageable);

  Optional<EventType> findByIdAndIsActiveTrue(UUID id);

  @Query("""
      SELECT b FROM Booking b
      WHERE b.eventDate >= CURRENT_DATE
      ORDER BY b.eventDate ASC
      """)
  List<Booking> findTop5UpcomingEvents(Pageable pageable);

  @Query("SELECT COUNT(b) FROM Booking b WHERE b.eventDate = CURRENT_DATE")
  long countTodayEvents();

}

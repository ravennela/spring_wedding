package com.example.online.event.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online.event.entity.EventType;

public interface EventTypeRepository extends JpaRepository<EventType, UUID> {

    List<EventType> findByIsActiveTrue();

    Optional<EventType> findByName(String name);
}

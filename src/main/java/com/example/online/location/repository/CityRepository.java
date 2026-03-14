package com.example.online.location.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online.location.entity.City;

public interface CityRepository extends JpaRepository<City, UUID> {

    List<City> findByIsActiveTrue();

    Optional<City> findByName(String name);

    Page<City> findByNameContainingIgnoreCase(String name, Pageable pageable);
}


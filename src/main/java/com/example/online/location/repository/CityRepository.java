package com.example.online.location.repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online.location.enitity.City;

public interface CityRepository extends JpaRepository<City, UUID> {

    List<City> findByIsActiveTrue();
    Optional<City> findByName(String name);
}

package com.example.online.vender.repository;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online.vender.entity.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, UUID> {

    Optional<Vendor> findByUserId(UUID userId);
}

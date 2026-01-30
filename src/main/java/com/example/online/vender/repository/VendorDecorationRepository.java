package com.example.online.vender.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online.vender.entity.VendorDecoration;

public interface VendorDecorationRepository
        extends JpaRepository<VendorDecoration, UUID> {

    List<VendorDecoration> findByVendorIdAndIsAvailableTrue(UUID vendorId);

    List<VendorDecoration> findByDecorationIdAndIsAvailableTrue(UUID decorationId);
}


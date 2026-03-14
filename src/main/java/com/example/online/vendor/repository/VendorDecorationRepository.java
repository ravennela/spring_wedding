package com.example.online.vendor.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online.vendor.entity.VendorDecoration;

public interface VendorDecorationRepository
        extends JpaRepository<VendorDecoration, UUID> {

    List<VendorDecoration> findByVendorIdAndIsAvailableTrue(UUID vendorId);

    List<VendorDecoration> findByDecorationIdAndIsAvailableTrue(UUID decorationId);
}






package com.example.online.vendor.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.online.admin.dto.VendorAssignementDto;
import com.example.online.common.enums.ServiceType;
import com.example.online.common.enums.VendorStatus;
import com.example.online.vendor.entity.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, UUID> {

    Optional<Vendor> findByUserId(UUID userId);

    List<Vendor> findByStatus(VendorStatus status);

    List<Vendor> findByServiceTypeAndStatus(ServiceType serviceType, VendorStatus status);

    @Query("""
            SELECT new com.example.online.admin.dto.VendorAssignementDto(
                v.id,
                v.user.name,
                v.companyName,
                v.user.phone,
                CASE WHEN bvr.id IS NOT NULL THEN true ELSE false END,
                CAST(v.serviceType AS string),
                v.city.name,
                v.address,
                v.description,
                v.isActive
            )
            FROM Vendor v
            LEFT JOIN BookingVendorRequest bvr
            ON v.user.id = bvr.vendor.id
            AND bvr.booking.id = :bookingId
            """)
    List<VendorAssignementDto> getVendorsWithAssignment(UUID bookingId);

    
    
}

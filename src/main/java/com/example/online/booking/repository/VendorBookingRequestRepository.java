package com.example.online.booking.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online.booking.entity.VendorBookingRequest;
import com.example.online.common.enums.RequestStatus;

public interface VendorBookingRequestRepository
        extends JpaRepository<VendorBookingRequest, UUID> {

    List<VendorBookingRequest> findByVendorId(UUID vendorId);

    List<VendorBookingRequest> findByBookingId(UUID bookingId);

    Optional<VendorBookingRequest> findByBookingIdAndVendorId(
            UUID bookingId,
            UUID vendorId
    );

    List<VendorBookingRequest> findByVendorIdAndStatus(
            UUID vendorId,
            RequestStatus status
    );
}

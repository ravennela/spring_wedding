package com.example.online.booking.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.online.booking.entity.BookingVendorRequest;
import com.example.online.common.enums.BookingStatus;
import com.example.online.common.enums.VendorRequestStatus;

public interface BookingVendorRequestRepository
                extends JpaRepository<BookingVendorRequest, Long> {

        Page<BookingVendorRequest> findByVendor_IdAndStatus(
                        UUID vendorId,
                        VendorRequestStatus status,
                        Pageable pageable);

        @Query("SELECT bvr FROM BookingVendorRequest bvr " +
                        "WHERE bvr.vendor.id = :vendorId " +
                        "AND bvr.status = :status " +
                        "AND bvr.booking.status = :bookingStatus")
        Page<BookingVendorRequest> findByVendorIdAndStatusAndBookingStatus(
                        @Param("vendorId") UUID vendorId,
                        @Param("status") VendorRequestStatus status,
                        @Param("bookingStatus") BookingStatus bookingStatus,
                        Pageable pageable);

        List<BookingVendorRequest> findByVendor_Id(UUID vendorId);

        List<BookingVendorRequest> findByBooking_Id(UUID bookingId);

        Optional<BookingVendorRequest> findByBooking_IdAndVendor_Id(
                        UUID bookingId,
                        UUID vendorId);

        boolean existsByBooking_IdAndVendor_Id(
                        UUID bookingId,
                        UUID vendorId);

        boolean existsByBookingIdAndVendorId(UUID bookingId, UUID vendorId);

        void deleteByBooking_IdAndVendor_Id(UUID bookingId, UUID vendorId);
}
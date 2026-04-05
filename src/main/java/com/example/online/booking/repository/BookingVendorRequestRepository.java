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

        int countByVendor_IdAndStatus(
                        UUID vendorId,
                        VendorRequestStatus status);

        @Query("SELECT bvr FROM BookingVendorRequest bvr " +
                        "WHERE bvr.vendor.id = :vendorId " +
                        "AND bvr.status = :status " +
                        "AND bvr.booking.status = :bookingStatus")
        Page<BookingVendorRequest> findByVendorIdAndStatusAndBookingStatus(
                        @Param("vendorId") UUID vendorId,
                        @Param("status") VendorRequestStatus status,
                        @Param("bookingStatus") BookingStatus bookingStatus,
                        Pageable pageable);

        @Query("SELECT COUNT(bvr) FROM BookingVendorRequest bvr " +
                        "WHERE bvr.vendor.id = :vendorId " +
                        "AND bvr.status = :status " +
                        "AND bvr.booking.status = :bookingStatus")
        int countByVendorIdAndStatusAndBookingStatus(
                        @Param("vendorId") UUID vendorId,
                        @Param("status") VendorRequestStatus status,
                        @Param("bookingStatus") BookingStatus bookingStatus);

        @Query("SELECT COALESCE(SUM(bvr.booking.totalAmount), 0) FROM BookingVendorRequest bvr " +
                        "WHERE bvr.vendor.id = :vendorId " +
                        "AND bvr.status = :status " +
                        "AND bvr.booking.status = :bookingStatus")
        java.math.BigDecimal sumEarningsByVendorId(
                        @Param("vendorId") UUID vendorId,
                        @Param("status") VendorRequestStatus status,
                        @Param("bookingStatus") BookingStatus bookingStatus);

        @Query("SELECT COALESCE(SUM(bvr.booking.totalAmount), 0) FROM BookingVendorRequest bvr " +
                        "WHERE bvr.vendor.id = :vendorId " +
                        "AND bvr.status = :status " +
                        "AND bvr.booking.status = :bookingStatus " +
                        "AND MONTH(bvr.booking.eventDate) = MONTH(CURRENT_DATE) " +
                        "AND YEAR(bvr.booking.eventDate) = YEAR(CURRENT_DATE)")
        java.math.BigDecimal sumThisMonthEarningsByVendorId(
                        @Param("vendorId") UUID vendorId,
                        @Param("status") VendorRequestStatus status,
                        @Param("bookingStatus") BookingStatus bookingStatus);

        @Query("SELECT COALESCE(SUM(bvr.booking.totalAmount), 0) FROM BookingVendorRequest bvr " +
                        "WHERE bvr.vendor.id = :vendorId " +
                        "AND bvr.status = :status " +
                        "AND bvr.booking.paymentStatus = :paymentStatus")
        java.math.BigDecimal sumPendingPaymentsByVendorId(
                        @Param("vendorId") UUID vendorId,
                        @Param("status") VendorRequestStatus status,
                        @Param("paymentStatus") com.example.online.common.enums.PaymentStatus paymentStatus);

        @Query("SELECT new com.example.online.vendor.dto.EarningItemDto(" +
                        "bvr.booking.id, bvr.booking.eventType.name, bvr.booking.eventDate, bvr.booking.totalAmount, bvr.booking.paymentStatus) " +
                        "FROM BookingVendorRequest bvr " +
                        "WHERE bvr.vendor.id = :vendorId " +
                        "AND bvr.status = :status " +
                        "ORDER BY bvr.booking.eventDate DESC")
        List<com.example.online.vendor.dto.EarningItemDto> findVendorEarnings(
                        @Param("vendorId") UUID vendorId,
                        @Param("status") VendorRequestStatus status);

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
        
        void deleteByBooking_Id(UUID bookingId);
}
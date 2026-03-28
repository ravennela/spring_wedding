
package com.example.online.booking.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.online.booking.entity.Booking;
import com.example.online.common.enums.BookingStatus;
import com.example.online.common.enums.PaymentStatus;
import com.example.online.user.entity.User;
import com.example.online.vendor.dto.EarningItemDto;
import com.example.online.vendor.dto.RecentJobDto;

import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID>, JpaSpecificationExecutor<Booking> {

        Page<Booking> findByCustomer(User customer, Pageable pageable);

        Optional<Booking> findByRazorpayOrderId(String razorpayOrderId);

        List<Booking> findByVendor(User vendor);

        Optional<Booking> findByIdAndCustomer(UUID id, User customer);

        @Query("""
                            SELECT b FROM Booking b
                            LEFT JOIN FETCH b.customer
                            LEFT JOIN FETCH b.eventType
                            LEFT JOIN FETCH b.decoration
                            LEFT JOIN FETCH b.city
                            LEFT JOIN FETCH b.vendor
                            LEFT JOIN FETCH b.address
                            WHERE b.id = :id
                        """)
        Optional<Booking> findBookingWithDetails(@Param("id") UUID id);

        long count();

        // today events
        long countByEventDate(LocalDate date);

        // booking status
        long countByStatus(BookingStatus status);

        // pending vendor assignment
        long countByVendorIsNull();

        // recent bookings
        List<Booking> findTop5ByOrderByCreatedAtDesc();

        @Query("""
                        SELECT FUNCTION('DAYNAME', b.eventDate), COUNT(b)
                        FROM Booking b
                        WHERE b.eventDate >= :startDate
                        GROUP BY FUNCTION('DAYNAME', b.eventDate)
                        ORDER BY MIN(b.eventDate)
                        """)
        List<Object[]> getWeeklyBookings(@Param("startDate") LocalDate startDate);

        @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'REQUESTED'")
        long countPendingActions();

        @Query("SELECT COUNT(b) FROM Booking b WHERE b.vendor IS NULL")
        long countPendingVendorAssignment();

        int countByVendorIdAndStatus(UUID vendorId, BookingStatus status);

        @Query("""
                        SELECT new com.example.online.vendor.dto.RecentJobDto(
                            b.id,
                            e.name,
                            c.name,
                            b.eventDate,
                            b.status,
                            b.totalAmount
                        )
                        FROM Booking b
                        JOIN b.eventType e
                        JOIN b.city c
                        WHERE b.vendor.id = :vendorId
                        ORDER BY b.createdAt DESC
                        """)
        List<RecentJobDto> findRecentJobs(UUID vendorId, Pageable pageable);

        @Query("""
                        SELECT COALESCE(SUM(b.totalAmount), 0)
                        FROM Booking b
                        WHERE b.vendor.id = :vendorId
                        AND b.status = com.example.online.common.enums.BookingStatus.COMPLETED
                        """)
        BigDecimal sumEarningsByVendorId(UUID vendorId);

        @Query("""
                        SELECT COALESCE(SUM(b.totalAmount),0)
                        FROM Booking b
                        WHERE b.vendor.id = :vendorId
                        AND b.status = com.example.online.common.enums.BookingStatus.COMPLETED
                        """)
        BigDecimal sumTotalEarnings(UUID vendorId);

        @Query("""
                        SELECT COALESCE(SUM(b.totalAmount),0)
                        FROM Booking b
                        WHERE b.vendor.id = :vendorId
                        AND b.status = com.example.online.common.enums.BookingStatus.COMPLETED
                        AND MONTH(b.eventDate) = MONTH(CURRENT_DATE)
                        AND YEAR(b.eventDate) = YEAR(CURRENT_DATE)
                        """)
        BigDecimal sumThisMonthEarnings(UUID vendorId);

        @Query("""
                        SELECT new com.example.online.vendor.dto.EarningItemDto(
                            b.id,
                            e.name,
                            b.eventDate,
                            b.totalAmount,
                            b.paymentStatus
                        )
                        FROM Booking b
                        JOIN b.eventType e
                        WHERE b.vendor.id = :vendorId
                        ORDER BY b.eventDate DESC
                        """)
        List<EarningItemDto> findVendorEarnings(UUID vendorId);

        @Query("""
                        SELECT COALESCE(SUM(b.totalAmount),0)
                        FROM Booking b
                        WHERE b.vendor.id = :vendorId
                        AND b.paymentStatus = com.example.online.common.enums.PaymentStatus.PENDING
                        """)
        BigDecimal sumPendingPayments(UUID vendorId);
}


package com.example.online.booking.repository;

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

}

package com.example.online.vendor.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online.vendor.entity.BookingDecoration;

public interface BookingDecorationRepository extends JpaRepository<BookingDecoration, UUID> {

    List<BookingDecoration> findByBookingId(UUID bookingId);

}





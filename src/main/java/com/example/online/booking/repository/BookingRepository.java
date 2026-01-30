
package com.example.online.booking.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online.booking.entity.Booking;
import com.example.online.user.entity.User;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerOrderByCreatedAtDesc(User customer);

    List<Booking> findByVendor(User vendor);
}



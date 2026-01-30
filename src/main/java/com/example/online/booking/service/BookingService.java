package com.example.online.booking.service;

import java.util.List;

import com.example.online.booking.dto.BookingCreateRequestDTO;
import com.example.online.booking.dto.BookingResponseDTO;
import com.example.online.booking.entity.Booking;
import com.example.online.user.entity.User;

public interface BookingService {

    BookingResponseDTO  createBooking(BookingCreateRequestDTO request);

    List<Booking> getMyBookings(User customer);

    void assignVendor(Long bookingId, Long vendorId);

    List<BookingResponseDTO> getAllMyBookings();
}

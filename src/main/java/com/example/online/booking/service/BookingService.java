package com.example.online.booking.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.online.booking.dto.BookingCreateRequestDto;
import com.example.online.booking.dto.BookingDetailsResponseDto;
import com.example.online.booking.dto.BookingResponseDto;
import com.example.online.booking.entity.Booking;
import com.example.online.user.entity.User;

public interface BookingService {

        BookingResponseDto createBooking(BookingCreateRequestDto request);

        Page<BookingResponseDto> getAllMyBookings(Pageable pageable);

        BookingDetailsResponseDto getBookingDetails(UUID bookingId);

        void cancelBooking(UUID bookingId);
}



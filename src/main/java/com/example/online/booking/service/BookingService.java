package com.example.online.booking.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.online.booking.dto.AdminBookingDetailResponseDTO;
import com.example.online.booking.dto.AdminBookingFilterRequest;
import com.example.online.booking.dto.AdminBookingListResponseDTO;
import com.example.online.booking.dto.BookingCreateRequestDTO;
import com.example.online.booking.dto.BookingDetailsResponseDTO;
import com.example.online.booking.dto.BookingResponseDTO;
import com.example.online.booking.entity.Booking;
import com.example.online.common.enums.BookingStatus;
import com.example.online.common.enums.PaymentStatus;
import com.example.online.user.entity.User;

public interface BookingService {

    BookingResponseDTO createBooking(BookingCreateRequestDTO request);

    List<Booking> getMyBookings(User customer);

    void assignVendor(Long bookingId, Long vendorId);

    Page<BookingResponseDTO> getAllMyBookings(Pageable pageable);

    BookingDetailsResponseDTO getBookingDetails(UUID bookingId);

    void cancelBooking(UUID bookingId);

    Page<AdminBookingListResponseDTO> getAllBookingsForAdmin(
            AdminBookingFilterRequest filter,
            Pageable pageable);

    AdminBookingDetailResponseDTO getBookingDetailsForAdmin(UUID bookingId);

    void updateBookingStatus(UUID bookingId, BookingStatus newStatus);
}

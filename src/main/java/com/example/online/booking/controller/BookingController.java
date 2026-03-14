package com.example.online.booking.controller;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.online.booking.dto.BookingCreateRequestDto;
import com.example.online.booking.dto.BookingDetailsResponseDto;
import com.example.online.booking.dto.BookingResponseDto;
import com.example.online.booking.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
        @Autowired
        private BookingService bookingService;

        @PostMapping
        @PreAuthorize("hasRole('CUSTOMER')")
        public ResponseEntity<BookingResponseDto> createBooking(
                        @Valid @RequestBody BookingCreateRequestDto request) {
                System.out.println("Received booking request: " + request);

                return new ResponseEntity<>(
                                bookingService.createBooking(request),
                                HttpStatus.CREATED);
        }

        @GetMapping("/my")
        @PreAuthorize("hasRole('CUSTOMER')")
        public ResponseEntity<Page<BookingResponseDto>> getMyBookings(
                        @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

                return ResponseEntity.ok(
                                bookingService.getAllMyBookings(pageable));
        }

        @GetMapping("/{bookingId}")
        @PreAuthorize("hasRole('CUSTOMER')")
        public ResponseEntity<BookingDetailsResponseDto> getBookingDetails(
                        @PathVariable UUID bookingId) {
                return ResponseEntity.ok(
                                bookingService.getBookingDetails(bookingId));
        }

        @PatchMapping("/{bookingId}/cancel")
        public ResponseEntity<?> cancelBooking(
                        @PathVariable UUID bookingId) {

                bookingService.cancelBooking(bookingId);
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Booking cancelled successfully");
                response.put("bookingId", bookingId);
                response.put("status", "CANCELLED");

                return ResponseEntity.ok(response);
        }
}




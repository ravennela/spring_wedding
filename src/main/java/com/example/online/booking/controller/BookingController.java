package com.example.online.booking.controller;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.online.booking.dto.BookingCreateRequestDTO;
import com.example.online.booking.dto.BookingResponseDTO;
import com.example.online.booking.service.BookingService;

@RestController
@RequestMapping("/bookings")
public class BookingController {

        private final BookingService bookingService;

        public BookingController(BookingService bookingService) {
                this.bookingService = bookingService;
        }

        /**
         * CUSTOMER → Create a booking
         */
        @PostMapping
        @PreAuthorize("hasRole('CUSTOMER')")
        public ResponseEntity<BookingResponseDTO> createBooking(
                        @Valid @RequestBody BookingCreateRequestDTO request) {

                return new ResponseEntity<>(
                                bookingService.createBooking(request),
                                HttpStatus.CREATED);
        }

        @GetMapping("/my")
        @PreAuthorize("hasRole('CUSTOMER')")
        public ResponseEntity<List<BookingResponseDTO>> getMyBookings() {

                return ResponseEntity.ok(bookingService.getAllMyBookings());
        }
}

package com.example.online.booking.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;

import com.example.online.booking.dto.AdminBookingDetailResponseDTO;
import com.example.online.booking.dto.AdminBookingFilterRequest;
import com.example.online.booking.dto.AdminBookingListResponseDTO;
import com.example.online.booking.dto.UpdateBookingStatusRequestDTO;
import com.example.online.booking.service.BookingService;
import com.example.online.common.enums.BookingStatus;
import com.example.online.common.enums.PaymentStatus;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/bookings")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping
    public ResponseEntity<Page<AdminBookingListResponseDTO>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) PaymentStatus paymentStatus) {

        AdminBookingFilterRequest filter = new AdminBookingFilterRequest();
        filter.setStatus(status);
        filter.setCity(city);
        filter.setPaymentStatus(paymentStatus);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return ResponseEntity.ok(
                bookingService.getAllBookingsForAdmin(filter, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminBookingDetailResponseDTO> getBookingDetails(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                bookingService.getBookingDetailsForAdmin(id));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateBookingStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBookingStatusRequestDTO request) {

        bookingService.updateBookingStatus(id, request.getStatus());

        return ResponseEntity.ok("Booking status updated successfully");
    }
}

package com.example.online.admin.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;

import com.example.online.booking.dto.AdminBookingDetailResponseDto;
import com.example.online.booking.dto.AdminBookingFilterRequest;
import com.example.online.booking.dto.AdminBookingListResponseDto;
import com.example.online.booking.dto.AdminCancelRequestDto;
import com.example.online.booking.dto.AssignVendorsRequestDto;
import com.example.online.booking.dto.UpdateBookingStatusRequestDto;
import com.example.online.admin.service.AdminBookingService;
import com.example.online.common.enums.BookingStatus;
import com.example.online.common.enums.PaymentStatus;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/bookings")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBookingController {
    @Autowired
    private AdminBookingService adminBookingService;

    @GetMapping
    public ResponseEntity<Page<AdminBookingListResponseDto>> getAllBookings(
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
                adminBookingService.getAllBookingsForAdmin(filter, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminBookingDetailResponseDto> getBookingDetails(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                adminBookingService.getBookingDetailsForAdmin(id));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBookingStatusRequestDto request) {

        adminBookingService.updateBookingStatus(id, request.getStatus());
        Map<String, String> response = Map.of("message", "Booking status updated successfully");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<?> cancelBooking(
            @PathVariable UUID bookingId,
            @Valid @RequestBody AdminCancelRequestDto request) {

        adminBookingService.adminCancelBooking(bookingId, request.getReason());
        Map<String, String> response = Map.of("message", "Booking cancelled successfully by admin");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{bookingId}/assign-vendors")
    public ResponseEntity<?> assignVendors(
            @PathVariable UUID bookingId,
            @RequestBody AssignVendorsRequestDto request) {

        adminBookingService.assignVendors(bookingId, request.getVendorIds());
        Map<String, String> response = Map.of("message", "Vendors assigned successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{bookingId}/vendors/{vendorId}")
    public ResponseEntity<?> deassignVendor(
            @PathVariable UUID bookingId,
            @PathVariable UUID vendorId) {

        adminBookingService.deassignVendor(bookingId, vendorId);
        Map<String, String> response = Map.of("message", "Vendor de-assigned successfully");

        return ResponseEntity.ok(response);
    }
}

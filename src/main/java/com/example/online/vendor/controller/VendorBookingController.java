package com.example.online.vendor.controller;

import java.util.Map;
import java.util.UUID;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.example.online.auth.security.CustomUserDetails;
import com.example.online.vendor.dto.BookingDetailsResponseDto;
import com.example.online.vendor.dto.VendorAcceptedBookingResponseDto;
import com.example.online.vendor.dto.VendorPendingBookingResponseDto;
import com.example.online.vendor.service.VendorBookingService;

@RestController
@RequestMapping("/vendor/bookings")
public class VendorBookingController {

        @Autowired
        private VendorBookingService vendorBookingService;

        @GetMapping("/pending")
        public Page<VendorPendingBookingResponseDto> getPendingBookings(
                        @AuthenticationPrincipal CustomUserDetails user,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {

                Pageable pageable = PageRequest.of(page, size);

                return vendorBookingService.getVendorPendingRequests(
                                user.getId(),
                                pageable);
        }

        @PatchMapping("/{bookingId}/accept")
        public ResponseEntity<?> acceptRequest(
                        @PathVariable UUID bookingId,
                        @AuthenticationPrincipal CustomUserDetails user) {

                vendorBookingService.acceptVendorRequest(
                                bookingId,
                                user.getId());

                return ResponseEntity.ok(
                                Map.of(
                                                "success", true,
                                                "message", "Booking accepted successfully"));
        }

        @GetMapping("/accepted")
        public Page<VendorAcceptedBookingResponseDto> getAcceptedBookings(
                        @AuthenticationPrincipal CustomUserDetails user,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {

                Pageable pageable = PageRequest.of(page, size);

                return vendorBookingService.getVendorAcceptedBookings(
                                user.getId(),
                                pageable);
        }

        @PatchMapping("/{bookingId}/complete")
        public ResponseEntity<?> completeBooking(
                        @PathVariable UUID bookingId,
                        @AuthenticationPrincipal CustomUserDetails user) {

                vendorBookingService.completeBooking(
                                bookingId,
                                user.getId());

                return ResponseEntity.ok(
                                Map.of("message", "Booking marked as completed"));
        }

        @GetMapping("/completed")
        public Page<VendorAcceptedBookingResponseDto> getCompletedBookings(
                        @AuthenticationPrincipal CustomUserDetails user,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {

                Pageable pageable = PageRequest.of(page, size);

                return vendorBookingService.getVendorCompletedBookings(
                                user.getId(),
                                pageable);
        }

        @GetMapping("/{bookingId}")
        public BookingDetailsResponseDto getBookingDetails(
                        @PathVariable UUID bookingId,
                        @AuthenticationPrincipal CustomUserDetails user) {
                return vendorBookingService.getBookingDetails(bookingId, user.getId());
        }
}

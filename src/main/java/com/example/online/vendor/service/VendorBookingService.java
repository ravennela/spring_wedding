package com.example.online.vendor.service;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.online.vendor.dto.BookingDetailsResponseDto;
import com.example.online.vendor.dto.UpdateVendorProfileRequest;
import com.example.online.vendor.dto.VendorAcceptedBookingResponseDto;
import com.example.online.vendor.dto.VendorDashboardResponseDto;
import com.example.online.vendor.dto.VendorEarningsResponseDto;
import com.example.online.vendor.dto.VendorPendingBookingResponseDto;
import com.example.online.vendor.dto.VendorProfileResponseDto;
import com.example.online.vendor.dto.VendorResponseDto;
import com.example.online.common.enums.ServiceType;
import com.example.online.common.enums.VendorStatus;
public interface VendorBookingService {
    BookingDetailsResponseDto getBookingDetails(UUID bookingId, UUID vendorId);

    Page<VendorPendingBookingResponseDto> getVendorPendingRequests(UUID vendorId, Pageable pageable);

    void acceptVendorRequest(UUID bookingId, UUID vendorId);

    Page<VendorAcceptedBookingResponseDto> getVendorAcceptedBookings(UUID vendorId, Pageable pageable);

    Page<VendorAcceptedBookingResponseDto> getVendorCompletedBookings(UUID vendorId, Pageable pageable);

    void completeBooking(UUID bookingId, UUID vendorId);
    
    public List<VendorResponseDto> getAllVendors(ServiceType serviceType,VendorStatus status);

    VendorProfileResponseDto getVendorProfile(UUID userId);
    void updateVendorProfile(UUID userId, UpdateVendorProfileRequest request);

    VendorDashboardResponseDto getDashboard(UUID userId);

     VendorEarningsResponseDto getEarnings(UUID userId);
}

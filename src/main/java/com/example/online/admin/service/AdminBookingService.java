package com.example.online.admin.service;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.online.admin.dto.VendorAssignementDto;
import com.example.online.booking.dto.AdminBookingDetailResponseDto;
import com.example.online.booking.dto.AdminBookingFilterRequest;
import com.example.online.booking.dto.AdminBookingListResponseDto;
import com.example.online.common.enums.BookingStatus;

public interface AdminBookingService {
    Page<AdminBookingListResponseDto> getAllBookingsForAdmin(AdminBookingFilterRequest filter, Pageable pageable);

    AdminBookingDetailResponseDto getBookingDetailsForAdmin(UUID bookingId);

    void updateBookingStatus(UUID bookingId, BookingStatus newStatus);

    void adminCancelBooking(UUID bookingId, String reason);

    void assignVendors(UUID bookingId, List<UUID> vendorIds);

    void deassignVendor(UUID bookingId, UUID vendorId);
    
    List<VendorAssignementDto> getVendorsForBooking(UUID bookingId);
}





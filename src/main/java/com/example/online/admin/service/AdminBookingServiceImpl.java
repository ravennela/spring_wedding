package com.example.online.admin.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.online.admin.dto.VendorAssignementDto;
import com.example.online.booking.dto.AdminBookingDetailResponseDto;
import com.example.online.booking.dto.AdminBookingFilterRequest;
import com.example.online.booking.dto.AdminBookingListResponseDto;
import com.example.online.booking.entity.Booking;
import com.example.online.booking.entity.BookingVendorRequest;
import com.example.online.booking.repository.BookingRepository;
import com.example.online.booking.repository.BookingVendorRequestRepository;
import com.example.online.booking.specifications.BookingSpecification;
import com.example.online.common.enums.BookingStatus;
import com.example.online.common.enums.VendorRequestStatus;
import com.example.online.user.entity.User;
import com.example.online.user.repository.UserRepository;
import com.example.online.vendor.repository.VendorRepository;

@Service
@Transactional
public class AdminBookingServiceImpl implements AdminBookingService {

    private final BookingRepository bookingRepository;
    private final BookingVendorRequestRepository bookingVendorRequestRepository;
    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;

    public AdminBookingServiceImpl(
            BookingRepository bookingRepository,
            BookingVendorRequestRepository bookingVendorRequestRepository,
            UserRepository userRepository,
            VendorRepository vendorRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingVendorRequestRepository = bookingVendorRequestRepository;
        this.userRepository = userRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public List<VendorAssignementDto> getVendorsForBooking(UUID bookingId) {
        return vendorRepository.getVendorsWithAssignment(bookingId);
    }

    @Override
    public Page<AdminBookingListResponseDto> getAllBookingsForAdmin(AdminBookingFilterRequest filter,
            Pageable pageable) {
        Specification<Booking> spec = BookingSpecification.filterBookings(filter);
        Page<Booking> bookings = bookingRepository.findAll(spec, pageable);
        return bookings.map(this::convertToAdminListDto);
    }

    @Override
    public AdminBookingDetailResponseDto getBookingDetailsForAdmin(UUID bookingId) {
        Booking booking = bookingRepository
                .findBookingWithDetails(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return mapToAdminDetailDto(booking);
    }

    @Override
    public void updateBookingStatus(UUID bookingId, BookingStatus newStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
        validateStatusTransition(booking.getStatus(), newStatus);
        booking.setStatus(newStatus);
        bookingRepository.save(booking);
    }

    @Override
    public void adminCancelBooking(UUID bookingId, String reason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Completed booking cannot be cancelled");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking already cancelled");
        }
        if (booking.getStatus() == BookingStatus.IN_PROGRESS) {
            throw new IllegalStateException("In-progress booking cannot be cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelReason(reason);
        booking.setCancelledBy("ADMIN");
        booking.setCancelledAt(LocalDateTime.now());
        bookingRepository.save(booking);
    }

    @Override
    public void assignVendors(UUID bookingId, List<UUID> vendorIds) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        for (UUID vendorId : vendorIds) {
            // Finding vendor by its own ID (from vendors table)
            com.example.online.vendor.entity.Vendor vendorRecord = vendorRepository.findById(vendorId)
                    .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + vendorId));

            User vendorUser = vendorRecord.getUser();

            boolean exists = bookingVendorRequestRepository
                    .existsByBooking_IdAndVendor_Id(bookingId, vendorUser.getId());
            if (exists)
                continue;

            // Set the vendor directly on the booking for now
            booking.setVendor(vendorUser);
            booking.setStatus(BookingStatus.VENDOR_ASSIGNED);

            BookingVendorRequest request = new BookingVendorRequest();
            request.setBooking(booking);
            request.setVendor(vendorUser);
            request.setStatus(VendorRequestStatus.PENDING); // Mark as accepted since admin is assigning
            request.setRequestedAt(LocalDateTime.now());
            request.setRespondedAt(LocalDateTime.now());
            bookingVendorRequestRepository.save(request);
        }
        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void deassignVendor(UUID bookingId, UUID vendorId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        com.example.online.vendor.entity.Vendor vendorRecord = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + vendorId));

        User vendorUser = vendorRecord.getUser();

        BookingVendorRequest request = bookingVendorRequestRepository
                .findByBooking_IdAndVendor_Id(bookingId, vendorUser.getId())
                .orElseThrow(() -> new RuntimeException("Vendor assignment not found"));

        if (request.getStatus() != VendorRequestStatus.PENDING) {
            throw new RuntimeException("Vendor cannot be de-assigned after accepting/rejecting");
        }

        // If booking currently has this vendor assigned → remove it
        if (booking.getVendor() != null &&
                booking.getVendor().getId().equals(vendorUser.getId())) {

            booking.setVendor(null);
            booking.setStatus(BookingStatus.REQUESTED);
            bookingRepository.save(booking);
        }

        // Delete vendor request
        bookingVendorRequestRepository.deleteByBooking_IdAndVendor_Id(bookingId, vendorUser.getId());
    }

    private AdminBookingListResponseDto convertToAdminListDto(Booking booking) {
        AdminBookingListResponseDto dto = new AdminBookingListResponseDto();
        dto.setBookingId(booking.getId());
        dto.setUserName(booking.getCustomer().getName());
        dto.setEventType(booking.getEventType().getName());
        dto.setEventDate(booking.getEventDate());
        dto.setCity(booking.getCity().getName());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setStatus(booking.getStatus());
        dto.setPaymentStatus(booking.getPaymentStatus());
        return dto;
    }

    private AdminBookingDetailResponseDto mapToAdminDetailDto(Booking booking) {
        AdminBookingDetailResponseDto dto = new AdminBookingDetailResponseDto();
        dto.setBookingId(booking.getId());
        dto.setBookingCode(booking.getBookingCode());
        dto.setCustomerName(booking.getCustomer().getName());
        dto.setCustomerEmail(booking.getCustomer().getEmail());
        dto.setCustomerPhone(booking.getCustomer().getPhone());
        dto.setEventType(booking.getEventType().getName());
        dto.setDecoration(booking.getDecoration() != null ? booking.getDecoration().getName() : null);
        dto.setEventDate(booking.getEventDate());
        dto.setCity(booking.getCity().getName());
        dto.setAddressLine(booking.getAddress() != null ? booking.getAddress().getLandmark() : null);
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setAdvanceAmount(booking.getAdvanceAmount());
        dto.setPaymentMode(booking.getPaymentMode());
        dto.setPaymentStatus(booking.getPaymentStatus());
        dto.setStatus(booking.getStatus());

        if (booking.getVendor() != null) {
            com.example.online.vendor.entity.Vendor vendorRecord = vendorRepository.findByUserId(booking.getVendor().getId()).orElse(null);
            if (vendorRecord != null) {
                dto.setVendorId(vendorRecord.getId());
                String vendorDisplayName = vendorRecord.getCompanyName() != null && !vendorRecord.getCompanyName().isEmpty()
                        ? vendorRecord.getCompanyName()
                        : booking.getVendor().getName();
                if (vendorDisplayName == null || vendorDisplayName.isEmpty()) {
                    vendorDisplayName = booking.getVendor().getPhone();
                }
                dto.setVendorName(vendorDisplayName);
            } else {
                dto.setVendorId(booking.getVendor().getId());
                dto.setVendorName(booking.getVendor().getName());
            }
        } else {
            dto.setVendorName("Not Assigned");
            dto.setVendorId(null);
        }

        dto.setCustomerNote(booking.getCustomerNote());
        dto.setCreatedAt(booking.getCreatedAt());
        return dto;
    }

    private void validateStatusTransition(BookingStatus current, BookingStatus next) {
        if (current == BookingStatus.CANCELLED)
            throw new IllegalStateException("Cannot change status of a cancelled booking");
        if (current == BookingStatus.COMPLETED)
            throw new IllegalStateException("Cannot change status of a completed booking");

        switch (current) {
            case REQUESTED -> {
                if (next != BookingStatus.VENDOR_ASSIGNED && next != BookingStatus.CANCELLED)
                    throw new IllegalStateException("Invalid transition");
            }
            case VENDOR_ASSIGNED -> {
                if (next != BookingStatus.COMPLETED && next != BookingStatus.CANCELLED)
                    throw new IllegalStateException("Invalid transition");
            }
            default -> throw new IllegalStateException("Invalid status transition");
        }
    }
}

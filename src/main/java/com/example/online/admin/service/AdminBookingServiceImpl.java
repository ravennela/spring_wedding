package com.example.online.admin.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.online.admin.dto.AdminDashboardDto;
import com.example.online.admin.dto.BookingOverviewDto;
import com.example.online.admin.dto.BookingStatusDto;
import com.example.online.admin.dto.DashboardStatsDto;
import com.example.online.admin.dto.PendingActionsDto;
import com.example.online.admin.dto.RecentBookingDto;
import com.example.online.admin.dto.UpcomingEventDto;
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
import com.example.online.event.repository.EventTypeRepository;
import com.example.online.payment.repository.PaymentRepository;
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
    private final EventTypeRepository eventTypeRepository;
    private final PaymentRepository paymentRepository;

    public AdminBookingServiceImpl(
            BookingRepository bookingRepository,
            BookingVendorRequestRepository bookingVendorRequestRepository,
            UserRepository userRepository,
            PaymentRepository paymentRepository,
            EventTypeRepository eventRepository,
            VendorRepository vendorRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingVendorRequestRepository = bookingVendorRequestRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.vendorRepository = vendorRepository;
        this.eventTypeRepository = eventRepository;
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
        dto.setDecorationId(booking.getDecoration() != null ? booking.getDecoration().getId() : null);
        dto.setEventDate(booking.getEventDate());
        dto.setCity(booking.getCity().getName());
        dto.setAddressLine(booking.getAddress() != null ? booking.getAddress().getLandmark() : null);
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setAdvanceAmount(booking.getAdvanceAmount());
        dto.setPaymentMode(booking.getPaymentMode());
        dto.setPaymentStatus(booking.getPaymentStatus());
        dto.setStatus(booking.getStatus());

        if (booking.getVendor() != null) {
            com.example.online.vendor.entity.Vendor vendorRecord = vendorRepository
                    .findByUserId(booking.getVendor().getId()).orElse(null);
            if (vendorRecord != null) {
                dto.setVendorId(vendorRecord.getId());
                String vendorDisplayName = vendorRecord.getCompanyName() != null
                        && !vendorRecord.getCompanyName().isEmpty()
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

        // Fetch all assigned vendors
        List<BookingVendorRequest> requests = bookingVendorRequestRepository.findByBooking_Id(booking.getId());
        List<AdminBookingDetailResponseDto.VendorInfo> assignedVendors = requests.stream()
                .map(req -> {
                    com.example.online.vendor.entity.Vendor vRec = vendorRepository
                            .findByUserId(req.getVendor().getId()).orElse(null);
                    String vName = (vRec != null && vRec.getCompanyName() != null && !vRec.getCompanyName().isEmpty())
                            ? vRec.getCompanyName()
                            : req.getVendor().getName();
                    return new AdminBookingDetailResponseDto.VendorInfo(
                            vRec != null ? vRec.getId() : req.getVendor().getId(), vName);
                })
                .toList();
        dto.setAssignedVendors(assignedVendors);

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

    @Override
    public AdminDashboardDto getDashboard() {
        DashboardStatsDto stats = getStats();

        // Booking Overview Chart
        List<BookingOverviewDto> bookingOverview = getBookingOverview();

        // Booking Status
        BookingStatusDto bookingStatus = getBookingStatus();

        // Recent Bookings
        List<RecentBookingDto> recentBookings = getRecentBookings();

        // Upcoming Events
        List<UpcomingEventDto> upcomingEvents = getUpcomingEvents(PageRequest.of(0, 5));

        // Pending Actions
        PendingActionsDto pendingActions = getPendingActions();

        AdminDashboardDto response = new AdminDashboardDto();
        response.setStats(stats);
        response.setBookingOverview(bookingOverview);
        response.setBookingStatus(bookingStatus);
        response.setRecentBookings(recentBookings);
        response.setUpcomingEvents(upcomingEvents);
        response.setPendingActions(pendingActions);

        return response;

    }

    private DashboardStatsDto getStats() {

        long totalBookings = bookingRepository.count();

        long todayEvents = eventTypeRepository.countTodayEvents();

        Double monthlyRevenue = paymentRepository.getMonthlyRevenue();

        long pendingActions = bookingRepository.countPendingActions();

        DashboardStatsDto dto = new DashboardStatsDto();
        dto.setTotalBookings(totalBookings);
        dto.setTodayEvents(todayEvents);
        dto.setMonthlyRevenue(monthlyRevenue != null ? monthlyRevenue : 0);
        dto.setPendingActions(pendingActions);
        return dto;
    }

    private List<BookingOverviewDto> getBookingOverview() {

        return bookingRepository.getWeeklyBookings(LocalDate.now().minusDays(6))
                .stream()
                .map(obj -> {
                    BookingOverviewDto dto = new BookingOverviewDto();
                    dto.setDay((String) obj[0]);
                    dto.setCount((Long) obj[1]);
                    return dto;
                })
                .toList();
    }

    private BookingStatusDto getBookingStatus() {

        long confirmed = bookingRepository.countByStatus(BookingStatus.CONFIRMED);
        long pending = bookingRepository.countByStatus(BookingStatus.IN_PROGRESS);
        long cancelled = bookingRepository.countByStatus(BookingStatus.CANCELLED);

        BookingStatusDto dto = new BookingStatusDto();
        dto.setConfirmed(confirmed);
        dto.setPending(pending);
        dto.setCancelled(cancelled);

        return dto;
    }

    private List<RecentBookingDto> getRecentBookings() {

        return bookingRepository.findTop5ByOrderByCreatedAtDesc()
                .stream()
                .map(booking -> {
                    RecentBookingDto dto = new RecentBookingDto();
                    dto.setBookingId(booking.getBookingCode());
                    dto.setCustomerName(booking.getCustomer() != null ? booking.getCustomer().getName() : "Unknown");
                    dto.setEventType(booking.getEventType() != null ? booking.getEventType().getName() : "Unknown");
                    dto.setStatus(booking.getStatus().name());
                    return dto;
                })
                .toList();
    }

    private List<UpcomingEventDto> getUpcomingEvents(Pageable pageable) {

        return eventTypeRepository.findTop5UpcomingEvents(pageable)
                .stream()
                .map(event -> {
                    UpcomingEventDto dto = new UpcomingEventDto();
                    dto.setTitle(event.getEventType().getName());
                    dto.setDate(event.getEventDate().toString());
                    // dto.setTime(event.getEventTime().toString());
                    dto.setVendorName(event.getVendor() != null ? event.getVendor().getName() : "Not Assigned");
                    dto.setStatus(event.getStatus());
                    return dto;
                })
                .toList();
    }

    private PendingActionsDto getPendingActions() {

        long vendorAssignments = bookingRepository.countPendingVendorAssignment();
        long paymentReviews = paymentRepository.countPendingPayments();

        PendingActionsDto dto = new PendingActionsDto();
        dto.setVendorAssignmentCount(vendorAssignments);
        dto.setPaymentReviewCount(paymentReviews);

        return dto;
    }
}

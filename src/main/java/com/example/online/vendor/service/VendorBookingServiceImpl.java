package com.example.online.vendor.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.online.booking.entity.Booking;
import com.example.online.booking.entity.BookingVendorRequest;
import com.example.online.booking.repository.BookingRepository;
import com.example.online.booking.repository.BookingVendorRequestRepository;
import com.example.online.common.enums.BookingStatus;
import com.example.online.common.enums.ServiceType;
import com.example.online.common.enums.VendorRequestStatus;
import com.example.online.common.enums.VendorStatus;
import com.example.online.location.entity.City;
import com.example.online.location.repository.CityRepository;
import com.example.online.user.entity.User;
import com.example.online.user.repository.UserRepository;
import com.example.online.vendor.dto.BookingDetailsResponseDto;
import com.example.online.vendor.dto.DecorationDto;
import com.example.online.vendor.dto.EarningItemDto;
import com.example.online.vendor.dto.RecentJobDto;
import com.example.online.vendor.dto.UpdateVendorProfileRequest;
import com.example.online.vendor.dto.VendorAcceptedBookingResponseDto;
import com.example.online.vendor.dto.VendorDashboardResponseDto;
import com.example.online.vendor.dto.VendorEarningsResponseDto;
import com.example.online.vendor.dto.VendorPendingBookingResponseDto;
import com.example.online.vendor.dto.VendorProfileResponseDto;
import com.example.online.vendor.dto.VendorResponseDto;
import com.example.online.vendor.entity.BookingDecoration;
import com.example.online.vendor.entity.Vendor;
import com.example.online.vendor.repository.BookingDecorationRepository;
import com.example.online.vendor.repository.VendorRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class VendorBookingServiceImpl implements VendorBookingService {

        private final BookingVendorRequestRepository bookingVendorRequestRepository;
        private final BookingRepository bookingRepository;
        private final UserRepository userRepository;
        private final CityRepository cityRepository;
        private final BookingDecorationRepository bookingDecorationRepository;

        private final VendorRepository vendorRepository;

        public VendorBookingServiceImpl(
                        BookingVendorRequestRepository bookingVendorRequestRepository,
                        BookingRepository bookingRepository,
                        UserRepository userRepository,
                        BookingDecorationRepository bookingDecorationRepository,
                        CityRepository cityRepository,
                        VendorRepository vendorRepository) {
                this.bookingVendorRequestRepository = bookingVendorRequestRepository;
                this.bookingRepository = bookingRepository;
                this.userRepository = userRepository;
                this.bookingDecorationRepository = bookingDecorationRepository;
                this.vendorRepository = vendorRepository;
                this.cityRepository = cityRepository;

        }

        public List<VendorResponseDto> getAllVendors(ServiceType serviceType, VendorStatus status) {

                List<Vendor> vendors;

                if (serviceType != null) {
                        vendors = vendorRepository.findByServiceTypeAndStatus(serviceType, status);
                } else {
                        vendors = vendorRepository.findByStatus(status != null ? status : VendorStatus.ACTIVE);
                }

                return vendors.stream()
                                .map(this::mapToDto)
                                .toList();
        }

        @Override
        public BookingDetailsResponseDto getBookingDetails(UUID bookingId, UUID vendorId) {
                boolean assigned = bookingVendorRequestRepository.existsByBookingIdAndVendorId(bookingId, vendorId);
                if (!assigned)
                        throw new RuntimeException("Vendor not assigned to this booking");

                Booking booking = bookingRepository.findById(bookingId)
                                .orElseThrow(() -> new RuntimeException("Booking not found"));
                User user = userRepository.findById(booking.getCustomer().getId())
                                .orElseThrow(() -> new RuntimeException("User not found"));
                List<BookingDecoration> bookingDecorations = bookingDecorationRepository.findByBookingId(bookingId);

                List<DecorationDto> decorationDtos = bookingDecorations.stream()
                                .map(decoration -> {
                                        DecorationDto dto = new DecorationDto();
                                        dto.setId(decoration.getId());
                                        dto.setName(decoration.getDecoration().getName());
                                        dto.setPrice(decoration.getDecoration().getBasePrice());
                                        dto.setQuantity(decoration.getQuantity());
                                        return dto;
                                })
                                .collect(Collectors.toList());

                // Price was showing 0 because of missing decorations, use booking.getTotalAmount() correctly
                BigDecimal totalPrice = booking.getTotalAmount() != null ? booking.getTotalAmount() : BigDecimal.ZERO;

                BookingDetailsResponseDto response = new BookingDetailsResponseDto();
                response.setBookingId(booking.getId());
                response.setStatus(booking.getStatus().name());
                response.setEventType(booking.getEventType().getName());
                response.setEventDate(booking.getEventDate());
                response.setCustomerName(user.getName());
                response.setCustomerPhone(user.getPhone());
                response.setDecorations(decorationDtos);
                response.setTotalPrice(totalPrice);

                return response;
        }

        @Override
        public VendorDashboardResponseDto getDashboard(UUID userId) {

                Vendor vendor = vendorRepository.findByUserId(userId)
                                .orElseThrow(() -> new RuntimeException("Vendor not found"));

                UUID vendorId = vendor.getId();

                // Dashboard was showing zeros due to incorrect status usage and data mismatch
                int pending = bookingVendorRequestRepository.countByVendor_IdAndStatus(
                                vendorId, VendorRequestStatus.PENDING);

                int accepted = bookingVendorRequestRepository.countByVendorIdAndStatusAndBookingStatus(
                                vendorId, VendorRequestStatus.ACCEPTED, BookingStatus.VENDOR_ASSIGNED);

                int completed = bookingVendorRequestRepository.countByVendorIdAndStatusAndBookingStatus(
                                vendorId, VendorRequestStatus.ACCEPTED, BookingStatus.COMPLETED);

                // Use the new booking vendor request based earnings sum to handle data inconsistency gracefully
                BigDecimal totalEarnings = bookingVendorRequestRepository.sumEarningsByVendorId(vendorId, VendorRequestStatus.ACCEPTED, BookingStatus.COMPLETED);

                List<RecentJobDto> recentJobs = bookingRepository.findRecentJobs(
                                vendorId,
                                PageRequest.of(0, 5));

                return new VendorDashboardResponseDto(
                                pending,
                                accepted,
                                completed,
                                totalEarnings != null
                                                ? totalEarnings.doubleValue()
                                                : 0.0,
                                recentJobs);
        }

        @Override
        public VendorEarningsResponseDto getEarnings(UUID userId) {

                Vendor vendor = vendorRepository.findByUserId(userId)
                                .orElseThrow(() -> new RuntimeException("Vendor not found"));

                UUID vendorId = vendor.getId();

                BigDecimal total = bookingVendorRequestRepository.sumEarningsByVendorId(vendorId, VendorRequestStatus.ACCEPTED, BookingStatus.COMPLETED);

                BigDecimal month = bookingVendorRequestRepository.sumThisMonthEarningsByVendorId(vendorId, VendorRequestStatus.ACCEPTED, BookingStatus.COMPLETED);

                BigDecimal pending = bookingVendorRequestRepository.sumPendingPaymentsByVendorId(vendorId, VendorRequestStatus.ACCEPTED, com.example.online.common.enums.PaymentStatus.PENDING);

                List<EarningItemDto> items = bookingVendorRequestRepository.findVendorEarnings(vendorId, VendorRequestStatus.ACCEPTED);

                return new VendorEarningsResponseDto(
                                total,
                                month,
                                pending,
                                items);
        }

        @Override
        public Page<VendorPendingBookingResponseDto> getVendorPendingRequests(UUID vendorId, Pageable pageable) {
                Page<BookingVendorRequest> page = bookingVendorRequestRepository.findByVendor_IdAndStatus(
                                vendorId, VendorRequestStatus.PENDING, pageable);
                return page.map(req -> new VendorPendingBookingResponseDto(
                                req.getBooking().getId(),
                                req.getBooking().getEventType().getName(),
                                req.getBooking().getDecoration().getName(),
                                req.getBooking().getEventDate(),
                                req.getBooking().getCity().getName(),
                                req.getBooking().getTotalAmount(),
                                req.getBooking().getStatus()));
        }

        @Override
        public void acceptVendorRequest(UUID bookingId, UUID vendorId) {
                BookingVendorRequest request = bookingVendorRequestRepository
                                .findByBooking_IdAndVendor_Id(bookingId, vendorId)
                                .orElseThrow(() -> new RuntimeException("Request not found"));

                if (request.getStatus() != VendorRequestStatus.PENDING)
                        throw new RuntimeException("Request already processed");

                Booking booking = request.getBooking();
                booking.setVendor(request.getVendor());
                booking.setStatus(BookingStatus.VENDOR_ASSIGNED);
                request.setStatus(VendorRequestStatus.ACCEPTED);

                bookingRepository.save(booking);
                bookingVendorRequestRepository.save(request);

                List<BookingVendorRequest> others = bookingVendorRequestRepository.findByBooking_Id(bookingId);
                for (BookingVendorRequest r : others) {
                        if (!r.getVendor().getId().equals(vendorId)) {
                                r.setStatus(VendorRequestStatus.REJECTED);
                        }
                }
                bookingVendorRequestRepository.saveAll(others);
        }

        @Override
        public Page<VendorAcceptedBookingResponseDto> getVendorAcceptedBookings(UUID vendorId, Pageable pageable) {
                Page<BookingVendorRequest> page = bookingVendorRequestRepository
                                .findByVendorIdAndStatusAndBookingStatus(
                                                vendorId, VendorRequestStatus.ACCEPTED, BookingStatus.VENDOR_ASSIGNED,
                                                pageable);

                return page.map(req -> new VendorAcceptedBookingResponseDto(
                                req.getBooking().getId(),
                                req.getBooking().getEventType().getName(),
                                req.getBooking().getDecoration() != null ? req.getBooking().getDecoration().getName()
                                                : null,
                                req.getBooking().getEventDate(),
                                req.getBooking().getCity().getName(),
                                req.getBooking().getTotalAmount(),
                                req.getBooking().getStatus()));
        }

        @Override
        public Page<VendorAcceptedBookingResponseDto> getVendorCompletedBookings(UUID vendorId, Pageable pageable) {
                Page<BookingVendorRequest> page = bookingVendorRequestRepository
                                .findByVendorIdAndStatusAndBookingStatus(
                                                vendorId, VendorRequestStatus.ACCEPTED, BookingStatus.COMPLETED,
                                                pageable);

                return page.map(req -> new VendorAcceptedBookingResponseDto(
                                req.getBooking().getId(),
                                req.getBooking().getEventType().getName(),
                                req.getBooking().getDecoration() != null ? req.getBooking().getDecoration().getName()
                                                : null,
                                req.getBooking().getEventDate(),
                                req.getBooking().getCity().getName(),
                                req.getBooking().getTotalAmount(),
                                req.getBooking().getStatus()));
        }

        @Override
        public void updateVendorProfile(UUID userId, UpdateVendorProfileRequest request) {

                Vendor vendor = vendorRepository.findByUserId(userId)
                                .orElseThrow(() -> new RuntimeException("Vendor not found"));

                User user = vendor.getUser();

                // update user fields
                user.setName(request.getName());
                user.setEmail(request.getEmail());

                // update city
                if (request.getCityId() != null) {
                        City city = cityRepository.findById(request.getCityId())
                                        .orElseThrow(() -> new RuntimeException("City not found"));
                        vendor.setCity(city);
                }

                // update vendor fields
                vendor.setCompanyName(request.getCompanyName());
                vendor.setAddress(request.getAddress());
                vendor.setDescription(request.getDescription());

                userRepository.save(user);
                vendorRepository.save(vendor);
        }

        @Override
        public VendorProfileResponseDto getVendorProfile(UUID userId) {

                Vendor vendor = vendorRepository.findByUserId(userId)
                                .orElseThrow(() -> new RuntimeException("Vendor not found"));

                User user = vendor.getUser();

                return new VendorProfileResponseDto(
                                user.getId().toString(),
                                user.getName(),
                                user.getEmail(),
                                user.getPhone(),

                                vendor.getCompanyName(),
                                vendor.getServiceType() != null ? vendor.getServiceType().name() : null,
                                vendor.getCity() != null ? vendor.getCity().getName() : null,
                                vendor.getAddress(),
                                vendor.getDescription());
        }

        @Override
        public void completeBooking(UUID bookingId, UUID vendorId) {
                Booking booking = bookingRepository.findById(bookingId)
                                .orElseThrow(() -> new RuntimeException("Booking not found"));
                if (!booking.getVendor().getId().equals(vendorId))
                        throw new RuntimeException("Unauthorized vendor");
                if (booking.getStatus() != BookingStatus.VENDOR_ASSIGNED)
                        throw new IllegalStateException("Only assigned bookings can be completed");

                booking.setStatus(BookingStatus.COMPLETED);
                bookingRepository.save(booking);
        }

        private VendorResponseDto mapToDto(Vendor vendor) {

                VendorResponseDto dto = new VendorResponseDto();

                dto.setName(vendor.getUser().getName());
                dto.setId(vendor.getUser().getId());
                dto.setPhone(vendor.getUser().getPhone());

                dto.setCompanyName(vendor.getCompanyName());
                dto.setServiceType(vendor.getServiceType());

                dto.setAddress(vendor.getAddress());
                dto.setDescription(vendor.getDescription());

                dto.setCity(
                                vendor.getCity() != null ? vendor.getCity().getName() : null);

                dto.setActive(vendor.isActive());

                dto.setRating(0.0); // or calculate later

                return dto;

        }
}

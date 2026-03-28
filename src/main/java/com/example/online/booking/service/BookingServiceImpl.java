package com.example.online.booking.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.online.address.entity.Address;
import com.example.online.address.repository.AddressRepository;
import com.example.online.booking.dto.AddressDto;
import com.example.online.booking.dto.BookingCreateRequestDto;
import com.example.online.booking.dto.BookingDetailsResponseDto;
import com.example.online.booking.dto.BookingResponseDto;
import com.example.online.booking.dto.PaymentDto;
import com.example.online.booking.dto.UpdateBookingRequest;
import com.example.online.booking.entity.Booking;
import com.example.online.booking.entity.BookingVendorRequest;
import com.example.online.booking.repository.BookingRepository;
import com.example.online.booking.repository.BookingVendorRequestRepository;
import com.example.online.common.enums.BookingStatus;
import com.example.online.common.enums.VendorRequestStatus;
import com.example.online.common.enums.PaymentMode;
import com.example.online.common.enums.PaymentStatus;
import com.example.online.event.entity.Decoration;
import com.example.online.event.entity.EventType;
import com.example.online.event.repository.DecorationRepository;
import com.example.online.event.repository.EventTypeRepository;
import com.example.online.vendor.repository.VendorRepository;
import com.example.online.location.entity.City;
import com.example.online.location.repository.CityRepository;
import com.example.online.payment.dto.RazorpayOrderResponse;
import com.example.online.payment.service.RazorpayService;
import com.example.online.user.entity.User;
import com.example.online.user.repository.UserRepository;
import com.example.online.vendor.dto.EarningItemDto;
import com.example.online.vendor.dto.VendorEarningsResponseDto;
import com.example.online.vendor.entity.Vendor;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final EventTypeRepository eventTypeRepository;
    private final DecorationRepository decorationRepository;
    private final CityRepository cityRepository;
    private final CurrentUserService currentUserService;

    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;
    private final BookingVendorRequestRepository bookingVendorRequestRepository;

    @Autowired
    private RazorpayService razorpayService;
    @Autowired
    private AddressRepository addressRepository;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            BookingVendorRequestRepository bookingVendorRequestRepository,
            EventTypeRepository eventTypeRepository,
            DecorationRepository decorationRepository,
            CityRepository cityRepository,
            UserRepository userRepository,
            VendorRepository vendorRepository,
            CurrentUserService currentUserService) {
        this.bookingRepository = bookingRepository;
        this.bookingVendorRequestRepository = bookingVendorRequestRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.decorationRepository = decorationRepository;
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
        this.vendorRepository = vendorRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public Page<BookingResponseDto> getAllMyBookings(Pageable pageable) {
        User customer = currentUserService.getCurrentUser();
        return bookingRepository
                .findByCustomer(customer, pageable)
                .map(this::mapToResponse);
    }

    private BookingResponseDto mapToResponse(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setBookingId(booking.getId().toString());
        dto.setEventType(booking.getEventType().getName());
        dto.setEventTime(booking.getEventTime());
        dto.setDecorationTitle(booking.getDecoration() != null ? booking.getDecoration().getName() : null);
        dto.setCity(booking.getCity().getName());
        dto.setEventDate(booking.getEventDate());
        dto.setStatus(booking.getStatus());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setCreatedAt(booking.getCreatedAt());
        return dto;
    }

    @Override
    public BookingResponseDto createBooking(BookingCreateRequestDto request) {
        User customer = currentUserService.getCurrentUser();
        System.out.println("Date Passed Validation Failed: " + request.getEventDate());

        if (request.getEventDate().isBefore(LocalDate.now())) {

            throw new IllegalArgumentException("Event date must be in the future");
        }
        System.out.println("Date Passed Validation: " + request.getEventDate());

        EventType eventType = eventTypeRepository.findById(request.getEventTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid event type"));
        System.out.println("Event Type Found: " + eventType.getName());

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid city"));
        System.out.println("City Found: " + city.getName());

        Decoration decoration = decorationRepository.findById(request.getDecorationId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid decoration"));
        System.out.println("Decoration Found: " + decoration.getName());

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid address"));

        System.out.println("Address Found: " + address.getArea());

        if (!address.getUser().getId().equals(customer.getId())) {
            throw new IllegalStateException("Address does not belong to current user");
        }
        System.out.println("Address belongs to current user");

        if (!decoration.isActive()) {
            throw new IllegalStateException("Selected decoration is not active");
        }
        System.out.println("Decoration is active");

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setEventType(eventType);
        booking.setCity(city);
        booking.setAddress(address);
        booking.setDecoration(decoration);
        booking.setEventTime(request.getEventTime());
        booking.setEventDate(request.getEventDate());
        booking.setCustomerNote(request.getCustomerNote());
        booking.setStatus(BookingStatus.REQUESTED);
        booking.setPaymentStatus(PaymentStatus.INITIATED);
        booking.setPaymentMode(request.getPaymentMode());
        booking.setTotalAmount(decoration.getBasePrice());

        System.out.println("Booking object created: " + booking);

        Booking savedBooking = bookingRepository.save(booking);

        if (request.getPaymentMode() == PaymentMode.ONLINE) {
            System.out.println("PaymentMode.ONLINE");
            RazorpayOrderResponse order = razorpayService.createOrder(savedBooking.getId(),
                    savedBooking.getTotalAmount());
            savedBooking.setRazorpayOrderId(order.getOrderId());
            bookingRepository.save(savedBooking);
            return mapToResponse(savedBooking);
        }

        return mapToResponse(savedBooking);
    }

    @Transactional
    public void updateBooking(UUID bookingId, UpdateBookingRequest request) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Update Vendors
        if (request.getVendorIds() != null) {
            // First, CLEAR all existing assignments to ensure consistency across screens
            bookingVendorRequestRepository.deleteByBooking_Id(bookingId);
            bookingVendorRequestRepository.flush(); // Force delete to DB before adding new ones
            
            if (request.getVendorIds().isEmpty()) {
                booking.setVendor(null);
                // RULE: If no vendor assigned, status must not be VENDOR_ASSIGNED
                if (booking.getStatus() == BookingStatus.VENDOR_ASSIGNED) {
                    booking.setStatus(BookingStatus.REQUESTED);
                }
            } else {
                Set<UUID> targetUserIds = new HashSet<>();
                for (String vId : request.getVendorIds()) {
                    if (vId == null || vId.isEmpty()) continue;
                    UUID potentialId = UUID.fromString(vId);
                    
                    User vendorUser = userRepository.findById(potentialId).orElse(null);
                    if (vendorUser == null) {
                        vendorUser = vendorRepository.findById(potentialId)
                            .map(Vendor::getUser)
                            .orElse(null);
                    }
                    
                    if (vendorUser != null) {
                        targetUserIds.add(vendorUser.getId());
                    }
                }

                // Apply assignments for unique users
                for (UUID uId : targetUserIds) {
                    User vUser = userRepository.findById(uId).orElse(null);
                    if (vUser != null) {
                        booking.setVendor(vUser); // Set last one as primary
                        
                        BookingVendorRequest bvr = new BookingVendorRequest();
                        bvr.setBooking(booking);
                        bvr.setVendor(vUser);
                        bvr.setStatus(VendorRequestStatus.PENDING);
                        bvr.setRequestedAt(java.time.LocalDateTime.now());
                        bookingVendorRequestRepository.save(bvr);
                    }
                }
                
                // RULE: If vendors are assigned, status must not be REQUESTED
                if (booking.getStatus() == BookingStatus.REQUESTED) {
                    booking.setStatus(BookingStatus.VENDOR_ASSIGNED);
                }
            }
        }

        // Update Decoration
        if (request.getDecorationId() != null) {
            if (request.getDecorationId().isEmpty()) {
                booking.setDecoration(null);
            } else {
                Decoration decoration = decorationRepository.findById(UUID.fromString(request.getDecorationId()))
                        .orElseThrow(() -> new RuntimeException("Decoration not found"));
                booking.setDecoration(decoration);
            }
        }

        // Update Price
        if (request.getTotalAmount() != null) {
            booking.setTotalAmount(request.getTotalAmount());
        }

        if (request.getAdvanceAmount() != null) {
            booking.setAdvanceAmount(request.getAdvanceAmount());
        }

        // Update Date
        if (request.getEventDate() != null) {
            booking.setEventDate(request.getEventDate());
        }

        // Update Time
        if (request.getEventTime() != null) {
            booking.setEventTime(request.getEventTime());
        }

        // Update Status (Handle only if not already set by vendor rules above)
        if (request.getStatus() != null) {
            // Apply Manual Status Update with Rules
            if (request.getStatus() == BookingStatus.VENDOR_ASSIGNED && booking.getVendor() == null) {
                // Ignore VENDOR_ASSIGNED if no vendor
            } else if (request.getStatus() == BookingStatus.REQUESTED && booking.getVendor() != null) {
                // Ignore REQUESTED if vendor exists
                booking.setStatus(BookingStatus.VENDOR_ASSIGNED);
            } else {
                booking.setStatus(request.getStatus());
            }
        }

        // Note
        if (request.getNote() != null) {
            booking.setCustomerNote(request.getNote());
        }

        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void cancelBooking(UUID bookingId) {
        User currentUser = currentUserService.getCurrentUser();
        Booking booking = bookingRepository
                .findByIdAndCustomer(bookingId, currentUser)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found or access denied"));

        if (booking.getEventDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Cannot cancel past events");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled");
        }

        if (booking.getStatus() != BookingStatus.REQUESTED) {
            throw new IllegalStateException("Only requested bookings can be cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        if (booking.getPaymentStatus() == PaymentStatus.SUCCESS) {
            booking.setPaymentStatus(PaymentStatus.REFUND_PENDING);
        }

        bookingRepository.save(booking);
    }

    @Override
    public BookingDetailsResponseDto getBookingDetails(UUID bookingId) {
        User currentUser = currentUserService.getCurrentUser();
        Booking booking = bookingRepository
                .findByIdAndCustomer(bookingId, currentUser)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found or access denied"));

        return mapToDetailsDto(booking);
    }

    private BookingDetailsResponseDto mapToDetailsDto(Booking booking) {
        BookingDetailsResponseDto dto = new BookingDetailsResponseDto();
        dto.setBookingId(booking.getId().toString());
        dto.setEventTitle(booking.getDecoration() != null ? booking.getDecoration().getName() : null);
        dto.setEventType(booking.getEventType() != null ? booking.getEventType().getName() : null);
        dto.setEventDate(booking.getEventDate());
        dto.setBookingStatus(booking.getStatus());
        dto.setPaymentStatus(booking.getPaymentStatus());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setPaymentMode(booking.getPaymentMode());
        dto.setBookingCreatedAt(booking.getCreatedAt());

        Address address = booking.getAddress();
        if (address != null) {
            AddressDto addressDto = new AddressDto();
            addressDto.setFullAddress(address.getHouseNo() + ", " + address.getArea() + ", " + address.getLandmark());
            addressDto.setCity(address.getCity());
            addressDto.setState(address.getState());
            addressDto.setPincode(address.getPincode());
            dto.setAddress(addressDto);
        }

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setRazorpayOrderId(booking.getRazorpayOrderId());
        paymentDto.setRazorpayPaymentId(booking.getRazorpayPaymentId());
        paymentDto.setPaymentStatus(booking.getPaymentStatus());
        paymentDto.setPaymentMode(booking.getPaymentMode());
        dto.setPayment(paymentDto);

        return dto;
    }

}

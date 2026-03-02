package com.example.online.booking.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.online.address.entity.Address;
import com.example.online.address.repository.AddressRepository;
import com.example.online.booking.dto.AddressDTO;
import com.example.online.booking.dto.AdminBookingDetailResponseDTO;
import com.example.online.booking.dto.AdminBookingFilterRequest;
import com.example.online.booking.dto.AdminBookingListResponseDTO;
import com.example.online.booking.dto.BookingCreateRequestDTO;
import com.example.online.booking.dto.BookingDetailsResponseDTO;
import com.example.online.booking.dto.BookingResponseDTO;
import com.example.online.booking.dto.PaymentDTO;
import com.example.online.booking.entity.Booking;
import com.example.online.booking.repository.BookingRepository;
import com.example.online.booking.specifications.BookingSpecification;
import com.example.online.common.enums.BookingStatus;
import com.example.online.common.enums.PaymentMode;
import com.example.online.common.enums.PaymentStatus;
import com.example.online.event.entity.Decoration;
import com.example.online.event.entity.EventType;
import com.example.online.event.repository.DecorationRepository;
import com.example.online.event.repository.EventTypeRepository;
import com.example.online.location.enitity.City;
import com.example.online.location.repository.CityRepository;
import com.example.online.payment.dto.RazorpayOrderResponse;
import com.example.online.payment.service.RazorpayService;
import com.example.online.user.entity.User;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final EventTypeRepository eventTypeRepository;
    private final DecorationRepository decorationRepository;
    private final CityRepository cityRepository;
    private final CurrentUserService currentUserService;
    @Autowired
    private RazorpayService razorpayService;
    @Autowired
    private AddressRepository addressRepository;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            EventTypeRepository eventTypeRepository,
            DecorationRepository decorationRepository,
            CityRepository cityRepository,
            CurrentUserService currentUserService) {

        this.bookingRepository = bookingRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.decorationRepository = decorationRepository;
        this.cityRepository = cityRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public Page<BookingResponseDTO> getAllMyBookings(Pageable pageable) {

        User customer = currentUserService.getCurrentUser();

        return bookingRepository
                .findByCustomer(customer, pageable)
                .map(this::mapToResponse);
    }

    private BookingResponseDTO mapToResponse(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        // ✅ UUID → String conversion happens HERE
        dto.setBookingId(booking.getId().toString());

        dto.setEventType(booking.getEventType().getName());
        dto.setDecorationTitle(
                booking.getDecoration() != null
                        ? booking.getDecoration().getName()
                        : null);
        dto.setCity(booking.getCity().getName());
        dto.setEventDate(booking.getEventDate());
        dto.setStatus(booking.getStatus());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setCreatedAt(booking.getCreatedAt());

        return dto;
    }

    @Override
    public AdminBookingDetailResponseDTO getBookingDetailsForAdmin(UUID bookingId) {

        Booking booking = bookingRepository
                .findBookingWithDetails(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        return mapToAdminDetailDTO(booking);
    }

    private AdminBookingDetailResponseDTO mapToAdminDetailDTO(Booking booking) {

        AdminBookingDetailResponseDTO dto = new AdminBookingDetailResponseDTO();

        dto.setBookingId(booking.getId());
        dto.setBookingCode(booking.getBookingCode());

        // Customer
        dto.setCustomerName(booking.getCustomer().getName());
        dto.setCustomerEmail(booking.getCustomer().getEmail());
        dto.setCustomerPhone(booking.getCustomer().getPhone());

        // Event
        dto.setEventType(booking.getEventType().getName());
        dto.setDecoration(
                booking.getDecoration() != null
                        ? booking.getDecoration().getName()
                        : null);
        dto.setEventDate(booking.getEventDate());

        // Location
        dto.setCity(booking.getCity().getName());
        dto.setAddressLine(
                booking.getAddress() != null
                        ? booking.getAddress().getLandmark()
                        : null);

        // Payment
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setAdvanceAmount(booking.getAdvanceAmount());
        dto.setPaymentMode(booking.getPaymentMode());
        dto.setPaymentStatus(booking.getPaymentStatus());

        // Status
        dto.setStatus(booking.getStatus());

        // Vendor
        dto.setVendorName(
                booking.getVendor() != null
                        ? booking.getVendor().getName()
                        : "Not Assigned");

        dto.setCustomerNote(booking.getCustomerNote());
        dto.setCreatedAt(booking.getCreatedAt());

        return dto;
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
    public Page<AdminBookingListResponseDTO> getAllBookingsForAdmin(
            AdminBookingFilterRequest filter,
            Pageable pageable) {

        Specification<Booking> spec = BookingSpecification.filterBookings(filter);

        Page<Booking> bookings = bookingRepository.findAll(spec, pageable);

        return bookings.map(this::convertToAdminListDTO);
    }

    private AdminBookingListResponseDTO convertToAdminListDTO(Booking booking) {

        AdminBookingListResponseDTO dto = new AdminBookingListResponseDTO();

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

    @Override
    public BookingResponseDTO createBooking(BookingCreateRequestDTO request) {
        System.out.println(" backend api hitted in");

        // 🔐 1. Get logged-in customer from JWT
        User customer = currentUserService.getCurrentUser();
        System.out.println(" customer fetched: " + customer.getId());

        // 📅 2. Validate event date
        if (request.getEventDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Event date must be in the future");
        }
        System.out.println(" event date validated: " + request.getEventDate());

        // 🎉 3. Fetch EventType
        EventType eventType = eventTypeRepository.findById(request.getEventTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid event type"));
        System.out.println(" event type fetched: " + eventType.getName());

        // 🏙️ 4. Fetch City
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid city"));
        System.out.println(" city fetched: " + city.getName());

        // 🎨 5. Fetch Decoration
        Decoration decoration = decorationRepository.findById(request.getDecorationId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid decoration"));

        System.out.println(" decoration fetched: " + decoration.getName());

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid address"));

        System.out.println(" address fetched: " + address.getId());
        if (!address.getUser().getId().equals(customer.getId())) {
            throw new IllegalStateException("Address does not belong to current user");
        }

        if (!decoration.isActive()) {
            throw new IllegalStateException("Selected decoration is not active");
        }
        System.out.println(" decoration is active: " + decoration.isActive());
        // 🧾 6. Create Booking
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setEventType(eventType);
        booking.setCity(city);
        booking.setAddress(address);
        booking.setDecoration(decoration);
        booking.setEventDate(request.getEventDate());
        booking.setCustomerNote(request.getCustomerNote());
        booking.setStatus(BookingStatus.REQUESTED);
        booking.setPaymentStatus(PaymentStatus.INITIATED);
        booking.setPaymentMode(request.getPaymentMode());
        booking.setTotalAmount(decoration.getBasePrice());
        System.out.println(" booking object created: " + booking);

        // 💾 7. SAVE (THIS WAS MISSING)
        Booking savedBooking = bookingRepository.save(booking);
        if (request.getPaymentMode() == PaymentMode.ONLINE) {
            System.out.println(" booking saved, now creating Razorpay order for booking ID: " + savedBooking.getId());

            RazorpayOrderResponse order = razorpayService.createOrder(
                    savedBooking.getId(),
                    savedBooking.getTotalAmount());
            System.out.println(" Razorpay order created: " + order.getOrderId());

            savedBooking.setRazorpayOrderId(order.getOrderId());
            bookingRepository.save(savedBooking);
            System.out.println("BookingServiceImpl.createBooking()");
            return mapToResponse(savedBooking);
        }
        System.out.println(" booking saved without online payment: " + savedBooking);
        // 🔁 8. Map & return DTO
        return mapToResponse(savedBooking);
    }

    @Override
    @Transactional
    public void cancelBooking(UUID bookingId) {

        // 🔐 1. Get current user
        User currentUser = currentUserService.getCurrentUser();

        // 🔐 2. Fetch booking securely
        Booking booking = bookingRepository
                .findByIdAndCustomer(bookingId, currentUser)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found or access denied"));

        // 📅 3. Prevent cancelling past events
        if (booking.getEventDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Cannot cancel past events");
        }

        // ❌ 4. Already cancelled
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled");
        }

        // 🚫 5. Business rule: only REQUESTED allowed (safe option)
        if (booking.getStatus() != BookingStatus.REQUESTED) {
            throw new IllegalStateException("Only requested bookings can be cancelled");
        }

        // 🔄 6. Update status
        booking.setStatus(BookingStatus.CANCELLED);

        // 💳 7. Payment handling (future-ready)
        if (booking.getPaymentStatus() == PaymentStatus.SUCCESS) {
            booking.setPaymentStatus(PaymentStatus.REFUND_PENDING);
            // You can create REFUND_PENDING enum later
        }

        bookingRepository.save(booking);
    }

    @Override
    public BookingDetailsResponseDTO getBookingDetails(UUID bookingId) {

        // 🔐 1. Get logged-in user
        User currentUser = currentUserService.getCurrentUser();

        // 🔐 2. Fetch booking securely
        Booking booking = bookingRepository
                .findByIdAndCustomer(bookingId, currentUser)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found or access denied"));

        // 🧱 3. Map to DTO
        return mapToDetailsDTO(booking);
    }

    private BookingDetailsResponseDTO mapToDetailsDTO(Booking booking) {

        BookingDetailsResponseDTO dto = new BookingDetailsResponseDTO();

        // 🔷 HERO
        dto.setBookingId(booking.getId().toString());
        dto.setEventTitle(
                booking.getDecoration() != null
                        ? booking.getDecoration().getName()
                        : null);
        dto.setEventType(
                booking.getEventType() != null
                        ? booking.getEventType().getName()
                        : null);
        dto.setEventDate(booking.getEventDate());
        dto.setBookingStatus(booking.getStatus());
        dto.setPaymentStatus(booking.getPaymentStatus());
        dto.setTotalAmount(booking.getTotalAmount());

        // 🔷 SUMMARY
        dto.setPaymentMode(booking.getPaymentMode());
        dto.setBookingCreatedAt(booking.getCreatedAt());

        // 🔷 EVENT INFO (Optional fields)
        // if available in entity

        // 🔷 ADDRESS
        Address address = booking.getAddress();
        if (address != null) {
            AddressDTO addressDTO = new AddressDTO();

            addressDTO.setVenueName(
                    booking.getDecoration() != null
                            ? booking.getDecoration().getName()
                            : null);

            addressDTO.setFullAddress(
                    address.getHouseNo() + ", " +
                            address.getArea() + ", " +
                            address.getLandmark());

            addressDTO.setCity(address.getCity());
            addressDTO.setState(address.getState());
            addressDTO.setPincode(address.getPincode());

            dto.setAddress(addressDTO);
        }

        // 🔷 PAYMENT
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setRazorpayOrderId(booking.getRazorpayOrderId());
        paymentDTO.setRazorpayPaymentId(booking.getRazorpayPaymentId());
        paymentDTO.setPaymentStatus(booking.getPaymentStatus());
        paymentDTO.setPaymentMode(booking.getPaymentMode());

        dto.setPayment(paymentDTO);

        return dto;
    }

    @Override
    public List<Booking> getMyBookings(User customer) {
        throw new UnsupportedOperationException("Unimplemented method 'getMyBookings'");
    }

    @Override
    public void assignVendor(Long bookingId, Long vendorId) {
        throw new UnsupportedOperationException("Unimplemented method 'assignVendor'");
    }

    private void validateStatusTransition(BookingStatus current, BookingStatus next) {

        if (current == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Cannot change status of a cancelled booking");
        }

        if (current == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot change status of a completed booking");
        }

        // Allowed transitions
        switch (current) {

            case REQUESTED -> {
                if (next != BookingStatus.APPROVED && next != BookingStatus.CANCELLED) {
                    throw new IllegalStateException("Invalid status transition from REQUESTED");
                }
            }

            case APPROVED -> {
                if (next != BookingStatus.VENDOR_ASSIGNED &&
                        next != BookingStatus.CANCELLED) {
                    throw new IllegalStateException("Invalid status transition from APPROVED");
                }
            }

            case VENDOR_ASSIGNED -> {
                if (next != BookingStatus.CONFIRMED &&
                        next != BookingStatus.CANCELLED) {
                    throw new IllegalStateException("Invalid status transition from VENDOR_ASSIGNED");
                }
            }

            case CONFIRMED -> {
                if (next != BookingStatus.IN_PROGRESS &&
                        next != BookingStatus.CANCELLED) {
                    throw new IllegalStateException("Invalid status transition from CONFIRMED");
                }
            }

            case IN_PROGRESS -> {
                if (next != BookingStatus.COMPLETED &&
                        next != BookingStatus.CANCELLED) {
                    throw new IllegalStateException("Invalid status transition from IN_PROGRESS");
                }
            }

            default -> throw new IllegalStateException("Invalid status transition");
        }
    }
}

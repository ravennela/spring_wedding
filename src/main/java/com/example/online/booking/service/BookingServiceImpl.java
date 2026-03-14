package com.example.online.booking.service;

import java.time.LocalDate;
import java.util.List;
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
import com.example.online.booking.entity.Booking;
import com.example.online.booking.repository.BookingRepository;
import com.example.online.common.enums.BookingStatus;
import com.example.online.common.enums.PaymentMode;
import com.example.online.common.enums.PaymentStatus;
import com.example.online.event.entity.Decoration;
import com.example.online.event.entity.EventType;
import com.example.online.event.repository.DecorationRepository;
import com.example.online.event.repository.EventTypeRepository;
import com.example.online.location.entity.City;
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

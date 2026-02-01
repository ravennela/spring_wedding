package com.example.online.booking.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.online.booking.dto.BookingCreateRequestDTO;
import com.example.online.booking.dto.BookingResponseDTO;
import com.example.online.booking.entity.Booking;
import com.example.online.booking.repository.BookingRepository;
import com.example.online.common.enums.BookingStatus;
import com.example.online.event.entity.Decoration;
import com.example.online.event.entity.EventType;
import com.example.online.event.repository.DecorationRepository;
import com.example.online.event.repository.EventTypeRepository;
import com.example.online.location.enitity.City;
import com.example.online.location.repository.CityRepository;
import com.example.online.user.entity.User;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final EventTypeRepository eventTypeRepository;
    private final DecorationRepository decorationRepository;
    private final CityRepository cityRepository;
    private final CurrentUserService currentUserService;

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
    public List<BookingResponseDTO> getAllMyBookings() {

        User customer = currentUserService.getCurrentUser();

        return bookingRepository.findByCustomerOrderByCreatedAtDesc(customer)
                .stream()
                .map(this::mapToResponse)
                .toList();
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
    public BookingResponseDTO createBooking(BookingCreateRequestDTO request) {

        // 🔐 1. Get logged-in customer from JWT
        User customer = currentUserService.getCurrentUser();

        // 📅 2. Validate event date
        if (request.getEventDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Event date must be in the future");
        }

        // 🎉 3. Fetch EventType
        EventType eventType = eventTypeRepository.findById(request.getEventTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid event type"));

        // 🏙️ 4. Fetch City
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid city"));

        // 🎨 5. Fetch Decoration
        Decoration decoration = decorationRepository.findById(request.getDecorationId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid decoration"));

        if (!decoration.isActive()) {
            throw new IllegalStateException("Selected decoration is not active");
        }

        // 🧾 6. Create Booking
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setEventType(eventType);
        booking.setCity(city);
        booking.setDecoration(decoration);
        booking.setEventDate(request.getEventDate());
        booking.setCustomerNote(request.getCustomerNote());
        booking.setStatus(BookingStatus.REQUESTED);
        booking.setTotalAmount(decoration.getBasePrice());

        // 💾 7. SAVE (THIS WAS MISSING)
        Booking savedBooking = bookingRepository.save(booking);

        // 🔁 8. Map & return DTO
        return mapToResponse(savedBooking);
    }

    @Override
    public List<Booking> getMyBookings(User customer) {
        throw new UnsupportedOperationException("Unimplemented method 'getMyBookings'");
    }

    @Override
    public void assignVendor(Long bookingId, Long vendorId) {
        throw new UnsupportedOperationException("Unimplemented method 'assignVendor'");
    }

}

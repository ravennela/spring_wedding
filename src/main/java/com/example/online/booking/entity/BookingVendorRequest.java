package com.example.online.booking.entity;

import java.time.LocalDateTime;

import com.example.online.common.enums.VendorRequestStatus;
import com.example.online.user.entity.User;

import jakarta.persistence.*;

@Entity

@Table(name = "booking_vendor_requests", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "booking_id", "vendor_id" })
})
public class BookingVendorRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private User vendor;

    @Enumerated(EnumType.STRING)
    private VendorRequestStatus status;

    private LocalDateTime requestedAt;

    private LocalDateTime respondedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public User getVendor() {
        return vendor;
    }

    public void setVendor(User vendor) {
        this.vendor = vendor;
    }

    public VendorRequestStatus getStatus() {
        return status;
    }

    public void setStatus(VendorRequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public LocalDateTime getRespondedAt() {
        return respondedAt;
    }

    public void setRespondedAt(LocalDateTime respondedAt) {
        this.respondedAt = respondedAt;
    }

    // getters setters
}
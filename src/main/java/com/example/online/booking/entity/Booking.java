package com.example.online.booking.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.online.common.entity.BaseEntity;
import com.example.online.common.enums.BookingStatus;
import com.example.online.event.entity.Decoration;
import com.example.online.event.entity.EventType;
import com.example.online.location.enitity.City;
import com.example.online.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookings")
public class Booking extends BaseEntity {

    // 👤 Customer who booked
    @ManyToOne(optional = false)
    private User customer;

    // 🎉 Event type (Wedding, Birthday, etc.)
    @ManyToOne(optional = false)
    private EventType eventType;

    // 🏙️ City of event
    @ManyToOne(optional = false)
    private City city;

    // 🎨 Selected decoration package
    @ManyToOne
    private Decoration decoration;

    // 📅 Event date
    @Column(nullable = false)
    private LocalDate eventDate;

    // 📌 Booking status
    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private BookingStatus status = BookingStatus.REQUESTED;

    // 📝 Optional customer note
    @Column(columnDefinition = "TEXT")
    private String customerNote;

    // 👤 Assigned vendor (later)
    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = true)
    private User vendor;

    // 💰 Final price (copied at booking time)
    @Column(name = "final_price", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "booking_code")
    private String bookingCode;

    @Column(name = "advance_amount")
    private BigDecimal  advanceAmount;

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Decoration getDecoration() {
        return decoration;
    }

    public void setDecoration(Decoration decoration) {
        this.decoration = decoration;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getCustomerNote() {
        return customerNote;
    }

    public void setCustomerNote(String customerNote) {
        this.customerNote = customerNote;
    }

    public User getVendor() {
        return vendor;
    }

    public void setVendor(User vendor) {
        this.vendor = vendor;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal  totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal  getAdvanceAmount() {
        return advanceAmount;
    }

    public void setAdvanceAmount(BigDecimal  advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    // -------- getters & setters ----------

}

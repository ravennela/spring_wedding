package com.example.online.booking.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.online.address.entity.Address;
import com.example.online.common.entity.BaseEntity;
import com.example.online.common.enums.BookingStatus;
import com.example.online.common.enums.PaymentMode;
import com.example.online.common.enums.PaymentStatus;
import com.example.online.event.entity.Decoration;
import com.example.online.event.entity.EventType;
import com.example.online.location.entity.City;
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

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "cancelled_by")
    private String cancelledBy; // "ADMIN" or "USER"

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    // 🏙️ City of event
    @ManyToOne(optional = false)
    private City city;

    // 🎨 Selected decoration package
    @ManyToOne
    private Decoration decoration;

    @Column(name = "event_time")
    private LocalTime eventTime;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode", length = 20)
    private PaymentMode paymentMode;

    // 💰 Payment status (INITIATED / SUCCESS / FAILED / REFUNDED)
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 30, nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    // Razorpay order id (for ONLINE only)
    @Column(name = "razorpay_order_id", unique = true)
    private String razorpayOrderId;

    // Razorpay payment id (after success)
    @Column(name = "razorpay_payment_id")
    private String razorpayPaymentId;

    @Column(name = "advance_amount")
    private BigDecimal advanceAmount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "address_id")
    private Address address;

    public String getBookingCode() {
        return bookingCode;
    }
    

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
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

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAdvanceAmount() {
        return advanceAmount;
    }

    public void setAdvanceAmount(BigDecimal advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }


    public LocalTime getEventTime() {
        return eventTime;
    }


    public void setEventTime(LocalTime eventTime) {
        this.eventTime = eventTime;
    }

    // -------- getters & setters ----------

}

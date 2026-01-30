package com.example.online.booking.entity;

import java.time.Instant;

import com.example.online.common.entity.BaseEntity;
import com.example.online.common.enums.RequestStatus;
import com.example.online.vender.entity.Vendor;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
  name = "vendor_booking_requests",
  uniqueConstraints = @UniqueConstraint(columnNames = {"booking_id", "vendor_id"})
)
public class VendorBookingRequest extends BaseEntity {

    @ManyToOne
    private Booking booking;

    @ManyToOne
    private Vendor vendor;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.SENT;

    private Instant respondedAt;

    public Booking getBooking() {
      return booking;
    }

    public void setBooking(Booking booking) {
      this.booking = booking;
    }

    public Vendor getVendor() {
      return vendor;
    }

    public void setVendor(Vendor vendor) {
      this.vendor = vendor;
    }

    public RequestStatus getStatus() {
      return status;
    }

    public void setStatus(RequestStatus status) {
      this.status = status;
    }

    public Instant getRespondedAt() {
      return respondedAt;
    }

    public void setRespondedAt(Instant respondedAt) {
      this.respondedAt = respondedAt;
    }
}

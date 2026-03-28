package com.example.online.booking.specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.online.booking.dto.AdminBookingFilterRequest;
import com.example.online.booking.entity.Booking;
import com.example.online.location.entity.City;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class BookingSpecification {
    public static Specification<Booking> filterBookings(AdminBookingFilterRequest filter) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }

            if (filter.getCity() != null && !filter.getCity().isEmpty()) {

                Join<Booking, City> cityJoin = root.join("city", JoinType.LEFT);

                predicates.add(
                        cb.like(
                                cb.lower(cityJoin.get("name")),
                                "%" + filter.getCity().toLowerCase() + "%"));
            }

            if (filter.getPaymentStatus() != null) {
                predicates.add(cb.equal(root.get("paymentStatus"), filter.getPaymentStatus()));
            }

            if (filter.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), filter.getStartDate()));
            }

            if (filter.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), filter.getEndDate()));
            }

            if (filter.getMinAmount() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalAmount"), filter.getMinAmount()));
            }

            if (filter.getMaxAmount() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalAmount"), filter.getMaxAmount()));
            }

            if (filter.getCustomerName() != null && !filter.getCustomerName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.join("customer", JoinType.LEFT).get("name")), 
                    "%" + filter.getCustomerName().toLowerCase() + "%"));
            }

            if (filter.getVendorName() != null && !filter.getVendorName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.join("vendor", JoinType.LEFT).get("name")), 
                    "%" + filter.getVendorName().toLowerCase() + "%"));
            }

            if (filter.getEventTypeId() != null) {
                predicates.add(cb.equal(root.join("eventType", JoinType.LEFT).get("id"), filter.getEventTypeId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}






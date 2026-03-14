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

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}






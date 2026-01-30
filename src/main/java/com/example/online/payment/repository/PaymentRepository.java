
package com.example.online.payment.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online.common.enums.PaymentStatus;
import com.example.online.common.enums.PaymentType;
import com.example.online.payment.entity.Payment;


public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByBookingId(UUID bookingId);

    Optional<Payment> findByGatewayOrderId(String gatewayOrderId);

    boolean existsByBookingIdAndPaymentTypeAndStatus(
            UUID bookingId,
            PaymentType paymentType,
            PaymentStatus status
    );
}

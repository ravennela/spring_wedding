
package com.example.online.payment.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.online.common.enums.PaymentStatus;
import com.example.online.common.enums.PaymentType;
import com.example.online.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByBookingId(UUID bookingId);

    Optional<Payment> findByGatewayOrderId(String gatewayOrderId);

    boolean existsByBookingIdAndPaymentTypeAndStatus(
            UUID bookingId,
            PaymentType paymentType,
            PaymentStatus status);

    @Query("""
            SELECT SUM(p.amount)
            FROM Payment p
            WHERE p.status = 'SUCCESS'
            AND MONTH(p.createdAt) = MONTH(CURRENT_DATE)
            AND YEAR(p.createdAt) = YEAR(CURRENT_DATE)
            """)
    Double getMonthlyRevenue();

    long countByStatus(PaymentStatus status);

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = 'PENDING'")
    long countPendingPayments();
}

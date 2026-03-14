package com.example.online.payment.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.online.booking.entity.Booking;
import com.example.online.booking.repository.BookingRepository;
import com.example.online.common.enums.BookingStatus;
import com.example.online.common.enums.PaymentStatus;
import com.example.online.payment.dto.RazorpayOrderResponse;

import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import com.razorpay.Order;
import org.json.JSONObject;

@Service
public class RazorpayServiceImpl implements RazorpayService {

    private final RazorpayClient razorpayClient;
    private final BookingRepository bookingRepository;

    @Value("${razorpay.key}")
    private String keyId;

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    public RazorpayServiceImpl(RazorpayClient razorpayClient, BookingRepository bookingRepository) {
        this.razorpayClient = razorpayClient;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public RazorpayOrderResponse createOrder(UUID bookingId, BigDecimal amount) {

        try {

            int amountInPaise = amount
                    .multiply(BigDecimal.valueOf(100))
                    .intValueExact();

            JSONObject options = new JSONObject();
            options.put("amount", amountInPaise);
            options.put("currency", "INR");
            options.put("receipt", bookingId.toString());
            options.put("payment_capture", 1);

            Order order = razorpayClient.orders.create(options);

            String orderId = order.get("id");

            // Save order id to booking for webhook verification
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));
            booking.setRazorpayOrderId(orderId);
            bookingRepository.save(booking);
            System.out.println("Creating Razorpay order for booking: " + bookingId);
            System.out.println("Amount in paise: " + amountInPaise);
            return new RazorpayOrderResponse(
                    orderId,
                    keyId,
                    amountInPaise);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Razorpay order creation failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void processWebhook(String payload, String signature) {
        try {

            boolean isValid = Utils.verifyWebhookSignature(
                    payload,
                    signature,
                    webhookSecret);

            if (!isValid) {
                throw new RuntimeException("Invalid Razorpay signature");
            }

            JSONObject json = new JSONObject(payload);
            String event = json.getString("event");

            JSONObject paymentEntity = json
                    .getJSONObject("payload")
                    .getJSONObject("payment")
                    .getJSONObject("entity");

            String razorpayOrderId = paymentEntity.getString("order_id");
            String razorpayPaymentId = paymentEntity.getString("id");

            Booking booking = bookingRepository
                    .findByRazorpayOrderId(razorpayOrderId)
                    .orElseThrow(() -> new RuntimeException("Booking not found for order id"));

            // 🔒 Idempotency protection
            if (booking.getPaymentStatus() == PaymentStatus.SUCCESS) {
                return;
            }

            if ("payment.captured".equals(event)) {

                booking.setPaymentStatus(PaymentStatus.SUCCESS);
                booking.setRazorpayPaymentId(razorpayPaymentId);
                booking.setStatus(BookingStatus.CONFIRMED);

            } else if ("payment.failed".equals(event)) {

                booking.setPaymentStatus(PaymentStatus.FAILED);
            }

            bookingRepository.save(booking);

        } catch (Exception e) {
            throw new RuntimeException("Webhook processing failed", e);
        }
    }
}

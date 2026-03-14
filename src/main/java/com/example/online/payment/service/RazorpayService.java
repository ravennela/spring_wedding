package com.example.online.payment.service;
import java.math.BigDecimal;
import java.util.UUID;

import com.example.online.payment.dto.RazorpayOrderResponse;

public interface RazorpayService {

    RazorpayOrderResponse createOrder(UUID bookingId, BigDecimal amount);

    void processWebhook(String payload, String signature);
}


package com.example.online.payment.controller;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.online.payment.dto.CreateOrderRequest;
import com.example.online.payment.dto.RazorpayOrderResponse;
import com.example.online.payment.service.RazorpayService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final RazorpayService razorpayService;

    public PaymentController(RazorpayService razorpayService) {
        this.razorpayService = razorpayService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature) {

        razorpayService.processWebhook(payload, signature);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create-order")
    public ResponseEntity<RazorpayOrderResponse> createOrder(
            @RequestBody CreateOrderRequest request) {

        RazorpayOrderResponse response = razorpayService.createOrder(
                request.getBookingId(),
                request.getAmount());

        return ResponseEntity.ok(response);
    }
}
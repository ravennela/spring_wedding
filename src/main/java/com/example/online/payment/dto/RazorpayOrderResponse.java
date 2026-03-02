package com.example.online.payment.dto;

import java.math.BigDecimal;

public class RazorpayOrderResponse {

    private String orderId;
    private String key;
    private int amount;

    public RazorpayOrderResponse(String orderId, String key, int amount) {
        this.orderId = orderId;
        this.key = key;
        this.amount = amount;
    }

    public String getOrderId() { return orderId; }
    public String getKey() { return key; }
    public int getAmount() { return amount; }
}
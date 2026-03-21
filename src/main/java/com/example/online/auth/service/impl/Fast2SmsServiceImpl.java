package com.example.online.auth.service.impl;

import com.example.online.auth.service.SmsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class Fast2SmsServiceImpl implements SmsService {

    @Value("${fast2sms.api.key}")
    private String apiKey;

    @Value("${fast2sms.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public Fast2SmsServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void sendSms(String phone, String message) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("authorization", apiKey)
                .queryParam("route", "q")
                .queryParam("message", message)
                .queryParam("flash", "0")
                .queryParam("numbers", phone)
                .toUriString();

        System.out.println("Calling Fast2SMS API: " + url.replaceAll("authorization=[^&]+", "authorization=****"));

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            System.out.println("Fast2SMS Response: " + response);
            if (response == null || !(boolean) response.get("return")) {
                throw new RuntimeException("Failed to send SMS via Fast2SMS: " + response);
            }
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR sending SMS via Fast2SMS: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error sending SMS", e);
        }
    }
}

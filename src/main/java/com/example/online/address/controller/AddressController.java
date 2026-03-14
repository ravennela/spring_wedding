package com.example.online.address.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.online.address.dto.AddressRequestDto;
import com.example.online.address.dto.AddressResponseDto;
import com.example.online.address.service.AddressService;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {
    @Autowired
    private AddressService addressService;

    // ✅ CREATE ADDRESS
    @PostMapping
    public ResponseEntity<AddressResponseDto> createAddress(
            @RequestBody AddressRequestDto request,
            @RequestParam UUID userId) {

        // assuming username = userId stored in JWT

        AddressResponseDto response = addressService.createAddress(userId, request);

        return ResponseEntity.ok(response);
    }

    // ✅ GET MY ADDRESSES
    @GetMapping("/my")
    public ResponseEntity<?> getMyAddresses(
            @RequestParam UUID userId) {

        List<AddressResponseDto> addresses = addressService.getUserAddresses(userId);

        return ResponseEntity.ok(addresses);
    }

    // ✅ DELETE ADDRESS
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable UUID id) {

        addressService.deleteAddress(id);

        return ResponseEntity.ok(
                Map.of("message", "Address deleted successfully"));
    }
}

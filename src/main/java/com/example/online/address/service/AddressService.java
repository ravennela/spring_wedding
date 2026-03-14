package com.example.online.address.service;

import java.util.List;
import java.util.UUID;

import com.example.online.address.dto.AddressRequestDto;
import com.example.online.address.dto.AddressResponseDto;

public interface AddressService {

    AddressResponseDto createAddress(
            UUID userId,
            AddressRequestDto request);

    List<AddressResponseDto> getUserAddresses(UUID userId);

    void deleteAddress(UUID addressId);
}

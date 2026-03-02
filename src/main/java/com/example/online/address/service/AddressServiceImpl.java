package com.example.online.address.service;

import com.example.online.address.dto.AddressRequestDto;
import com.example.online.address.dto.AddressResponseDto;
import com.example.online.address.entity.Address;
import com.example.online.address.repository.AddressRepository;
import com.example.online.address.service.AddressService;
import com.example.online.user.entity.User;
import com.example.online.user.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

@Transactional
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    // ✅ CREATE ADDRESS
    @Override
    public AddressResponseDto createAddress(
            UUID userId,
            AddressRequestDto request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // ✅ If new address is default → remove old default
        if (request.isDefault()) {

            addressRepository.findByUserAndDeletedFalse(user)
                    .forEach(address -> {
                        address.setDefault(false);
                        addressRepository.save(address);
                    });
        }

        Address address = new Address();

        address.setUser(user);
        address.setFullName(request.getFullName());
        address.setMobileNumber(request.getMobileNumber());
        address.setHouseNo(request.getHouseNo());
        address.setArea(request.getArea());
        address.setLandmark(request.getLandmark());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setAddressType(request.getAddressType());
        address.setDefault(request.isDefault());

        Address savedAddress = addressRepository.save(address);

        return mapToResponse(savedAddress);
    }

    // ✅ GET USER ADDRESSES
    @Override
    @Transactional(readOnly = true)
    public List<AddressResponseDto> getUserAddresses(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return addressRepository.findByUserAndDeletedFalse(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ DELETE ADDRESS
    @Override
    public void deleteAddress(UUID addressId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        address.setDeleted(true);
        addressRepository.save(address);
    }

    // ✅ DTO MAPPER
    private AddressResponseDto mapToResponse(Address address) {

        AddressResponseDto response = new AddressResponseDto();

        response.setId(address.getId());
        response.setFullName(address.getFullName());
        response.setMobileNumber(address.getMobileNumber());
        response.setHouseNo(address.getHouseNo());
        response.setArea(address.getArea());
        response.setLandmark(address.getLandmark());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setPincode(address.getPincode());
        response.setAddressType(address.getAddressType());
        response.setDefault(address.isDefault());

        return response;
    }
}
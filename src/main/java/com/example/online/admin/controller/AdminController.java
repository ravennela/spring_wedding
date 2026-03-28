package com.example.online.admin.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.online.admin.dto.AdminDashboardDto;
import com.example.online.admin.dto.VendorAssignementDto;
import com.example.online.admin.service.AdminBookingService;
import com.example.online.common.enums.ServiceType;
import com.example.online.common.enums.VendorStatus;
import com.example.online.vendor.dto.VendorResponseDto;
import com.example.online.vendor.service.VendorBookingService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private VendorBookingService vendorService;
    @Autowired
    private AdminBookingService adminBookingService;

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDto> dashboard() {
        return ResponseEntity.ok(adminBookingService.getDashboard());
    }

    @GetMapping("/vendors")
    public ResponseEntity<List<VendorResponseDto>> getAllVendors(
            @RequestParam(required = false) ServiceType serviceType,
            @RequestParam(required = false) VendorStatus vendorStatus) {

        List<VendorResponseDto> vendors = vendorService.getAllVendors(serviceType, vendorStatus);
        return ResponseEntity.ok(vendors);
    }

    @GetMapping("/vendors/assignments")
    public ResponseEntity<?> fetchVendorAssignList(@RequestParam UUID bookingId) {
        List<VendorAssignementDto> vendors = adminBookingService.getVendorsForBooking(bookingId);

        return ResponseEntity.ok(vendors);

    }
}

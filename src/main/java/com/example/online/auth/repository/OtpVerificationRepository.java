
package com.example.online.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online.auth.entity.OtpVerification;

public interface OtpVerificationRepository
        extends JpaRepository<OtpVerification, java.util.UUID> {

    Optional<OtpVerification> findTopByPhoneOrderByCreatedAtDesc(String phone);
}

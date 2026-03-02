
package com.example.online.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.online.auth.entity.OtpVerification;

public interface OtpVerificationRepository
        extends JpaRepository<OtpVerification, java.util.UUID> {

    Optional<OtpVerification> findFirstByPhoneOrderByCreatedAtDesc(String phone);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM OtpVerification o WHERE o.phone = :phone")
    void deleteByPhone(@Param("phone") String phone);
}

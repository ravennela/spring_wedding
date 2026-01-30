package com.example.online.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online.user.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByPhone(String mobileNumber);

    boolean existsByPhone(String mobileNumber);
}
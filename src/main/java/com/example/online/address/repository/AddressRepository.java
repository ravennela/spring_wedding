
package com.example.online.address.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online.address.entity.Address;
import com.example.online.user.entity.User;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    List<Address> findByUserAndDeletedFalse(User user);

    Optional<Address> findByUserAndIsDefaultTrue(User user);
}
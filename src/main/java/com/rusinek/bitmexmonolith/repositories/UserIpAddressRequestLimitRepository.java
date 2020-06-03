package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.limits.UserIpAddressRequestLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Adrian Rusinek on 02.06.2020
 **/
public interface UserIpAddressRequestLimitRepository extends JpaRepository<UserIpAddressRequestLimit, Long> {

    Optional<UserIpAddressRequestLimit> findByIpAddress(String ipAddress);
}


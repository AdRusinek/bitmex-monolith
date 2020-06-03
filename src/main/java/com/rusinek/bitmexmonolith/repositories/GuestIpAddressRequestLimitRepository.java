package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.limits.GuestIpAddressRequestLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Adrian Rusinek on 01.06.2020
 **/
public interface GuestIpAddressRequestLimitRepository extends JpaRepository<GuestIpAddressRequestLimit, Long> {

    Optional<GuestIpAddressRequestLimit> findByIpAddress(String ipAddress);
}


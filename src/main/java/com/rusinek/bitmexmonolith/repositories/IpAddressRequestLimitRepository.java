package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.limits.IpAddressRequestLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Adrian Rusinek on 01.06.2020
 **/
public interface IpAddressRequestLimitRepository extends JpaRepository<IpAddressRequestLimit,Long> {

    Optional<IpAddressRequestLimit> findByIpAddress(String ipAddress);
}


package com.rusinek.bitmexmonolith.services;

import com.rusinek.bitmexmonolith.model.limits.IpAddressRequestLimit;
import com.rusinek.bitmexmonolith.repositories.IpAddressRequestLimitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Adrian Rusinek on 01.06.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class IpAddressService {

    private final IpAddressRequestLimitRepository ipRepository;

    public boolean areIpRequestsOverloaded(String ipAddress) {

        if (ipAddress != null) {
            Optional<IpAddressRequestLimit> optionalIp = ipRepository.findByIpAddress(ipAddress);

            if (!optionalIp.isPresent()) {
                long currentTime = System.currentTimeMillis();
                IpAddressRequestLimit ipAddressRequestLimit = new IpAddressRequestLimit();
                ipAddressRequestLimit.setBlockadeActivatedAt(currentTime / 1000);
                ipAddressRequestLimit.setApiReadyToUse(currentTime / 1000);
                ipAddressRequestLimit.setIpAddress(ipAddress);
                ipAddressRequestLimit.setActionAttempts(1);

                ipRepository.save(ipAddressRequestLimit);

                return false;
            }

            IpAddressRequestLimit foundIp = optionalIp.get();

            if (foundIp.getApiReadyToUse() > System.currentTimeMillis() / 1000) {
                return true;
            }
            if (foundIp.getActionAttempts() < 5) {
                foundIp.setActionAttempts(foundIp.getActionAttempts() + 1);
                ipRepository.save(foundIp);
                return false;
            }
            if (foundIp.getActionAttempts() >= 5) {
                long currentTime = System.currentTimeMillis();
                foundIp.setBlockadeActivatedAt(currentTime / 1000);
                foundIp.setApiReadyToUse(currentTime / 1000 + 600);
                foundIp.setActionAttempts(0);
                ipRepository.save(foundIp);
                return true;
            }
        }
        return false;
    }
}
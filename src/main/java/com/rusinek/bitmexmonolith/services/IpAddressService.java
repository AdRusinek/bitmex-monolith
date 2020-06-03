package com.rusinek.bitmexmonolith.services;

import com.rusinek.bitmexmonolith.model.limits.GuestIpAddressRequestLimit;
import com.rusinek.bitmexmonolith.model.limits.UserIpAddressRequestLimit;
import com.rusinek.bitmexmonolith.repositories.GuestIpAddressRequestLimitRepository;
import com.rusinek.bitmexmonolith.repositories.UserIpAddressRequestLimitRepository;
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

    private final UserIpAddressRequestLimitRepository userIpRepository;
    private final GuestIpAddressRequestLimitRepository guestIpRepository;

    public boolean areUserIpRequestsOverloaded(String ipAddress) {

        if (ipAddress != null) {
            Optional<UserIpAddressRequestLimit> optionalIp = userIpRepository.findByIpAddress(ipAddress);

            if (!optionalIp.isPresent()) {
                long currentTime = System.currentTimeMillis();
                UserIpAddressRequestLimit userIpAddressRequestLimit = new UserIpAddressRequestLimit();
                userIpAddressRequestLimit.setBlockadeActivatedAt(currentTime / 1000);
                userIpAddressRequestLimit.setApiReadyToUse(currentTime / 1000);
                userIpAddressRequestLimit.setIpAddress(ipAddress);
                userIpAddressRequestLimit.setActionAttempts(1);
                userIpRepository.save(userIpAddressRequestLimit);
                return false;
            }

            UserIpAddressRequestLimit foundIp = optionalIp.get();

            if (foundIp.getApiReadyToUse() > System.currentTimeMillis() / 1000) {
                return true;
            }

            if (foundIp.getActionAttempts() < 5) {
                foundIp.setActionAttempts(foundIp.getActionAttempts() + 1);
                userIpRepository.save(foundIp);
                return false;
            }

            if (foundIp.getActionAttempts() >= 5) {
                long currentTime = System.currentTimeMillis();
                foundIp.setBlockadeActivatedAt(currentTime / 1000);
                foundIp.setApiReadyToUse(currentTime / 1000 + 600);
                foundIp.setActionAttempts(0);
                userIpRepository.save(foundIp);
                return true;
            }
        }
        return false;
    }


    public boolean areGuestIpRequestsOverloaded(String ipAddress) {

        if (ipAddress != null) {
            Optional<GuestIpAddressRequestLimit> optionalIp = guestIpRepository.findByIpAddress(ipAddress);

            if (!optionalIp.isPresent()) {
                long currentTime = System.currentTimeMillis();
                GuestIpAddressRequestLimit guestIpAddressRequestLimit = new GuestIpAddressRequestLimit();
                guestIpAddressRequestLimit.setBlockadeActivatedAt(currentTime / 1000);
                guestIpAddressRequestLimit.setApiReadyToUse(currentTime / 1000);
                guestIpAddressRequestLimit.setIpAddress(ipAddress);
                guestIpAddressRequestLimit.setActionAttempts(1);
                guestIpRepository.save(guestIpAddressRequestLimit);
                return false;
            }

            GuestIpAddressRequestLimit foundIp = optionalIp.get();

            if (foundIp.getApiReadyToUse() > System.currentTimeMillis() / 1000) {
                return true;
            }

            if (foundIp.getActionAttempts() < 5) {
                foundIp.setActionAttempts(foundIp.getActionAttempts() + 1);
                guestIpRepository.save(foundIp);
                return false;
            }

            if (foundIp.getActionAttempts() >= 5) {
                long currentTime = System.currentTimeMillis();
                foundIp.setBlockadeActivatedAt(currentTime / 1000);
                foundIp.setApiReadyToUse(currentTime / 1000 + 1800);
                foundIp.setActionAttempts(0);
                guestIpRepository.save(foundIp);
                return true;
            }
        }
        return false;
    }
}
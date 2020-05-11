package com.rusinek.bitmexmonolith.services;

import com.rusinek.bitmexmonolith.model.requestlimits.AccountRequestLimit;
import com.rusinek.bitmexmonolith.model.requestlimits.UserRequestLimit;
import com.rusinek.bitmexmonolith.repositories.AccountRequestLimitRepository;
import com.rusinek.bitmexmonolith.repositories.UserRequestLimitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by Adrian Rusinek on 08.05.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class LimitService {

    private final UserRequestLimitRepository userRequestLimitRepository;
    private final AccountRequestLimitRepository accountRequestLimitRepository;

    UserRequestLimit saveUserRequestLimit() {
        // setting request limits for newly registered users
        UserRequestLimit userRequestLimit = new UserRequestLimit();
        userRequestLimit.setBlockadeActivatedAt(0);
        userRequestLimit.setConnectionTestLimit(1);
        userRequestLimit.setApiReadyToUse(System.currentTimeMillis() / 1000L);
        log.debug("Setting default limits for new user.");
        return userRequestLimitRepository.save(userRequestLimit);
    }

    AccountRequestLimit saveAccountRequestLimit() {

        AccountRequestLimit accountRequestLimit = new AccountRequestLimit();
        accountRequestLimit.setBlockadeActivatedAt(0);
        accountRequestLimit.setApiReadyToUse(System.currentTimeMillis() / 1000L);
        log.debug("Setting default limits for new account.");
        return accountRequestLimitRepository.save(accountRequestLimit);
    }

}

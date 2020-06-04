package com.rusinek.bitmexmonolith.services;

import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.model.Stop;
import com.rusinek.bitmexmonolith.repositories.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Adrian Rusinek on 03.06.2020
 **/

public abstract class AbstractStopService<T extends Stop> {

    void bindStopOrder(T stopOrder, Principal principal, String accountId, UserRepository userRepository, AccountService accountService) {
        // checks if the user exists and after it finds it will bind stop to specific account
        userRepository.findByUsername(principal.getName()).ifPresent(user -> {
            stopOrder.setStopOwner(user.getUsername());

            Account account = accountService.findByAccountIdAndOwner(Long.valueOf(accountId), principal.getName());
            stopOrder.setAccount(account);
            stopOrder.setStopOwner(account.getAccountOwner());
        });
    }

    List<T> getAllByOwnerAndAccountId(String accountId, List<T> allByStopOwner) {
        // returns  stops that belongs to specific account
        return allByStopOwner.stream().filter(stopOrder ->
                stopOrder.getAccount().getId().equals(Long.valueOf(accountId)))
                .collect(Collectors.toList());
    }

    Account findAccount(String accountId, Principal principal, AccountService accountService) {
        // checks if provided account exists
        return accountService.findByAccountIdAndOwner(Long.valueOf(accountId), principal.getName());
    }
}

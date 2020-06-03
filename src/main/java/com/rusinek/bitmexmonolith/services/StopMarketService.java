package com.rusinek.bitmexmonolith.services;

import com.rusinek.bitmexmonolith.exceptions.stopOrderExceptions.StopMarketAmountException;
import com.rusinek.bitmexmonolith.exceptions.stopOrderExceptions.TrailingStopAmountException;
import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.model.StopMarket;
import com.rusinek.bitmexmonolith.repositories.StopMarketRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Adrian Rusinek on 03.06.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class StopMarketService {

    private final StopMarketRepository stopMarketRepository;
    private final UserRepository userRepository;
    private final AccountService accountService;

    public StopMarket saveStopMarket(StopMarket stopMarket, Principal principal, String accountId) {

        if (getAllByOwnerAndAccountId(principal, accountId).size() > 3) {
            throw new StopMarketAmountException("Stop Market orders amount on your account exceeded.");
        }

        // checks if the user exists and after it finds it will bind stop market to specific account
        userRepository.findByUsername(principal.getName()).ifPresent(user -> {
            stopMarket.setStopOwner(user.getUsername());

            Account account = accountService.findByAccountIdAndOwner(Long.valueOf(accountId), principal.getName());
            stopMarket.setAccount(account);
            stopMarket.setStopOwner(account.getAccountOwner());
        });

        return stopMarketRepository.save(stopMarket);

    }

    public List<StopMarket> getAllByOwnerAndAccountId(Principal principal, String accountId) {
        // gets all trailing stops bind to specific user
        List<StopMarket> allByTrailingStopOwner = (List<StopMarket>)
                stopMarketRepository.findAllByStopOwner(principal.getName());

        // returns trailing stops that belongs to specific account
        return allByTrailingStopOwner.stream().filter(stopMarket ->
                stopMarket.getAccount().getId().equals(Long.valueOf(accountId)))
                .collect(Collectors.toList());
    }

    public void deleteStopMarket(String accountId, String stopMarketId, Principal principal) {
        // checks if provided account exists
        Account account = accountService.findByAccountIdAndOwner(Long.valueOf(accountId), principal.getName());

        if (account != null) {
            stopMarketRepository.deleteById(Long.valueOf(stopMarketId));
        }
    }
}

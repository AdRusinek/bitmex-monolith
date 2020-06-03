package com.rusinek.bitmexmonolith.services;


import com.rusinek.bitmexmonolith.exceptions.stopOrderExceptions.TrailingStopAmountException;
import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.model.TrailingStop;
import com.rusinek.bitmexmonolith.repositories.TrailingStopRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Adrian Rusinek on 25.02.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class TrailingStopService {

    private final TrailingStopRepository trailingStopRepository;
    private final UserRepository userRepository;
    private final AccountService accountService;

    public TrailingStop saveTrailingStop(TrailingStop trailingStop, Principal principal, String accountId) {

        if (getAllByOwnerAndAccountId(principal, accountId).size() > 5) {
            throw new TrailingStopAmountException("Trailing Stop amount on your account exceeded.");
        }

        // checks if the user exists and after it finds it will bind trailing stop to specific account
        userRepository.findByUsername(principal.getName()).ifPresent(user -> {
            trailingStop.setStopOwner(user.getUsername());

            Account account = accountService.findByAccountIdAndOwner(Long.valueOf(accountId), principal.getName());
            trailingStop.setAccount(account);
            trailingStop.setStopOwner(account.getAccountOwner());
        });

        return trailingStopRepository.save(trailingStop);
    }

    public List<TrailingStop> getAllByOwnerAndAccountId(Principal principal, String accountId) {
        // gets all trailing stops bind to specific user
        List<TrailingStop> allByTrailingStopOwner = (List<TrailingStop>)
                trailingStopRepository.findAllByStopOwner(principal.getName());

        // returns trailing stops that belongs to specific account
        return allByTrailingStopOwner.stream().filter(trailingStop ->
                trailingStop.getAccount().getId().equals(Long.valueOf(accountId)))
                .collect(Collectors.toList());
    }

    public void deleteTrailingStop(String accountId, String trailingId, Principal principal) {
        // checks if provided account exists
        Account account = accountService.findByAccountIdAndOwner(Long.valueOf(accountId), principal.getName());

        if (account != null) {
            trailingStopRepository.deleteById(Long.valueOf(trailingId));
        }
    }
}

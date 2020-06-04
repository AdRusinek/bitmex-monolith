package com.rusinek.bitmexmonolith.services;


import com.rusinek.bitmexmonolith.exceptions.stopOrderExceptions.TrailingStopAmountException;
import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.model.TrailingStop;
import com.rusinek.bitmexmonolith.repositories.TrailingStopRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

/**
 * Created by Adrian Rusinek on 25.02.2020
 **/
@Service
@RequiredArgsConstructor
public class TrailingStopService extends AbstractStopService<TrailingStop> {

    private final TrailingStopRepository trailingStopRepository;
    private final UserRepository userRepository;
    private final AccountService accountService;

    public TrailingStop saveTrailingStopOrder(TrailingStop stopOrder, Principal principal, String accountId) {

        if (findAllAccountTrailingStopOrders(principal, accountId).size() > 5) {
            throw new TrailingStopAmountException("Trailing Stop amount on your account exceeded.");
        }

        super.bindStopOrder(stopOrder, principal, accountId, userRepository, accountService);

        return trailingStopRepository.save(stopOrder);
    }

    public List<TrailingStop> findAllAccountTrailingStopOrders(Principal principal, String accountId) {
        // gets all  stops bind to specific user
        List<TrailingStop> allByStopOwner = (List<TrailingStop>)
                trailingStopRepository.findAllByStopOwner(principal.getName());

        return super.getAllByOwnerAndAccountId(accountId, allByStopOwner);
    }

    public void deleteTrailingStopOrder(String accountId, String trailingId, Principal principal) {

        Account account = super.findAccount(accountId, principal, accountService);

        if (account != null) {
            trailingStopRepository.deleteById(Long.valueOf(trailingId));
        }
    }
}

package com.rusinek.bitmexmonolith.services;

import com.rusinek.bitmexmonolith.exceptions.stopOrderExceptions.StopMarketAmountException;
import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.model.StopMarket;
import com.rusinek.bitmexmonolith.repositories.StopMarketRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

/**
 * Created by Adrian Rusinek on 03.06.2020
 **/
@Service
@RequiredArgsConstructor
public class StopMarketService extends AbstractStopService<StopMarket> {


    private final StopMarketRepository stopMarketRepository;
    private final UserRepository userRepository;
    private final AccountService accountService;


    public StopMarket saveStopMarketOrder(StopMarket stopOrder, Principal principal, String accountId) {

        if (findAllAccountStopMarketOrders(principal, accountId).size() > 3) {
            throw new StopMarketAmountException("Stop Market orders amount on your account exceeded.");
        }

        super.bindStopOrder(stopOrder, principal, accountId, userRepository, accountService);

        return stopMarketRepository.save(stopOrder);
    }

    public List<StopMarket> findAllAccountStopMarketOrders(Principal principal, String accountId) {
        // gets all  stops bind to specific user
        List<StopMarket> allByStopOwner = (List<StopMarket>)
                stopMarketRepository.findAllByStopOwner(principal.getName());

        return super.getAllByOwnerAndAccountId(accountId, allByStopOwner);
    }

    public void deleteStopMarketOrder(String accountId, String trailingId, Principal principal) {

        Account account = super.findAccount(accountId, principal, accountService);

        if (account != null) {
            stopMarketRepository.deleteById(Long.valueOf(trailingId));
        }
    }
}

package com.rusinek.bitmexmonolith.services.exchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mashape.unirest.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.model.requestlimits.ExchangeRequestLimit;
import com.rusinek.bitmexmonolith.model.response.ExchangeError;
import com.rusinek.bitmexmonolith.repositories.AccountRepository;
import com.rusinek.bitmexmonolith.repositories.ExchangeRequestLimitRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by Adrian Rusinek on 09.05.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {

    private final AccountRepository accountRepository;
    private final ExchangeRequestLimitRepository exchangeRequestLimitRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    void manageAccountLimits(HttpResponse<String> response, Account account) {
        try {
            // reads how many request are left to exceed limit
            String limitHeader = String.valueOf(response.getHeaders().get("X-RateLimit-Remaining"));
            if (limitHeader == null) {
                // lock account on 10 seconds if something unexpected happened (idk what BitMEX returns if downtime...)
                account.getAccountRequestLimit().setApiReadyToUse(account.getAccountRequestLimit().getBlockadeActivatedAt() + 10);
            } else {
                int limit = Integer.valueOf(limitHeader.substring(1, limitHeader.length() - 1));
                log.info("User " + account.getAccountOwner() + " has " + limit + " requests left.");
                // limit 2 just to play it safe
                if (limit <= 2) {
                    // from this time it will check if 30 seconds have passed
                    account.getAccountRequestLimit().setBlockadeActivatedAt(System.currentTimeMillis() / 1000L);
                    account.getAccountRequestLimit().setApiReadyToUse(account.getAccountRequestLimit().getBlockadeActivatedAt() + 30);

                    accountRepository.save(account);
                    log.error("User '" + account.getAccountOwner() + "' with account Id '" + account.getId() + "' almost exceeded limit.");
                }
            }
        } catch (Exception e) {
            // could not resolve limits from header (BitMEX is probably down)
            ExchangeError exchangeError;
            try {
                exchangeError = objectMapper.readValue(response.getBody(), new TypeReference<ExchangeError>() {
                });

                log.error("Problem with getting lasting limits. BitMEX returned error: '" + exchangeError.getError().getName() +
                        "', with message '" + exchangeError.getError().getMessage() + "'. " + "Error happened to user " + account.getAccountOwner() + ".");

                // set field to false so ExchangeService won't be able to make a call
                Optional<ExchangeRequestLimit> limit = exchangeRequestLimitRepository.findById(1L);

                limit.ifPresent(exchangeRequestLimit -> {
                    ExchangeRequestLimit requestLimit = limit.get();
                    requestLimit.setBlockadeActivatedAt(System.currentTimeMillis() / 1000L);
                    requestLimit.setApiReadyToUse(requestLimit.getBlockadeActivatedAt() + 20);

                    exchangeRequestLimitRepository.save(requestLimit);
                });

            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
        }
    }

    void manageUserLimits(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {

            user.getUserRequestLimit().setConnectionTestLimit(user.getUserRequestLimit().getConnectionTestLimit() + 1);
            userRepository.save(user);

            if (user.getUserRequestLimit().getConnectionTestLimit() >= 5) {
                // it has to wait 120 seconds if the limit is exceeded
                user.getUserRequestLimit().setBlockadeActivatedAt(System.currentTimeMillis() / 1000L);
                user.getUserRequestLimit().setApiReadyToUse(user.getUserRequestLimit().getBlockadeActivatedAt() + 120);
                // set limit back to 0
                user.getUserRequestLimit().setConnectionTestLimit(0);

                userRepository.save(user);
                log.error("User '" + user.getUsername() + "' exceeded limit by testing connection.");
            }
        });
    }

    void refreshUserLimits(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.getUserRequestLimit().setConnectionTestLimit(0);
            userRepository.save(user);
        });
    }

}

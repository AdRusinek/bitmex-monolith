package com.rusinek.bitmexmonolith.services.exchange;

import com.mashape.unirest.http.HttpResponse;
import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.repositories.AccountRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by Adrian Rusinek on 09.05.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    void manageAccountLimits(HttpResponse<String> response, Account account) {
        // reads how many request are left to exceed limit
        String limitHeader = String.valueOf(response.getHeaders().get("X-RateLimit-Remaining"));
        int limit = Integer.valueOf(limitHeader.substring(1, limitHeader.length() - 1));
        System.out.println(limit);
        // limit 5 just to play it safe
        if (limit <= 5) {
            // from this time it will check if 60 seconds have passed
            account.getAccountRequestLimit().setBlockadeActivatedAt(System.currentTimeMillis() / 1000L);
            account.getAccountRequestLimit().setApiReadyToUse(account.getAccountRequestLimit().getBlockadeActivatedAt() + 60);

            accountRepository.save(account);
            log.error("User '" + account.getAccountOwner() + "' with account Id + '" + account.getId() + "'almost exceeded limit.");
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

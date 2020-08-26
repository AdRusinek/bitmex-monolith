package com.rusinek.bitmexmonolith.services;

import com.rusinek.bitmexmonolith.exceptions.authenticationException.ToManyLoginFailuresException;
import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.model.security.LoginFailure;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import com.rusinek.bitmexmonolith.repositories.security.LoginFailureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Adrian Rusinek on 27.08.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class UserLockService {

    private final LoginFailureRepository loginFailureRepository;
    private final UserRepository userRepository;

    public void lockUserAccount(User user) {
        List<LoginFailure> failures = loginFailureRepository.findAllByUserAndCreatedDateIsAfter(user,
                Timestamp.valueOf(LocalDateTime.now().minusDays(1)));

        if (failures.size() > 3) {
            log.debug("Locking User Account.");
            user.setAccountNonLocked(false);
            userRepository.save(user);

            throw new ToManyLoginFailuresException("Twoje konto zostało zablokowane " +
                    "na dzien ze względu na zbyt dużą liczbę nieudanych prób logowania.");
        }
    }
}

package com.rusinek.bitmexmonolith.security.listeners;

import com.rusinek.bitmexmonolith.exceptions.authenticationException.ToManyLoginFailuresException;
import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.model.security.LoginFailure;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import com.rusinek.bitmexmonolith.repositories.security.LoginFailureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Adrian Rusinek on 24.08.2020
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener {

    private final LoginFailureRepository loginFailureRepository;
    private final UserRepository userRepository;

    @EventListener
    public void listenFailure(AuthenticationFailureBadCredentialsEvent event) {
        log.debug("User failed to login");

        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();

            LoginFailure.LoginFailureBuilder builder = LoginFailure.builder();

            if (token.getPrincipal() instanceof String) {

                log.debug("User name: " + token.getPrincipal());

                builder.username((String) token.getPrincipal());
                userRepository.findByUsername((String) token.getPrincipal()).ifPresent(builder::user);
            }

            Object details =
                    SecurityContextHolder.getContext().getAuthentication().getDetails();

            if (details instanceof WebAuthenticationDetails) {

                builder.sourceIp(((WebAuthenticationDetails) details).getRemoteAddress());
            }
            LoginFailure failure = loginFailureRepository.save(builder.build());
            log.debug("Failure Event: " + failure.getId());

            if (failure.getUser() != null) {
                lockUserAccount(failure.getUser());
            }
        }
    }

    private void lockUserAccount(User user) {
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

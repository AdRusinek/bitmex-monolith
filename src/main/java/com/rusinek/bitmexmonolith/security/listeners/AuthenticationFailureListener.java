package com.rusinek.bitmexmonolith.security.listeners;

import com.rusinek.bitmexmonolith.model.security.LoginFailure;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import com.rusinek.bitmexmonolith.repositories.security.LoginFailureRepository;
import com.rusinek.bitmexmonolith.services.UserLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * Created by Adrian Rusinek on 24.08.2020
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener {

    private final LoginFailureRepository loginFailureRepository;
    private final UserRepository userRepository;
    private final UserLockService userLockService;

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
                userLockService.lockUserAccount(failure.getUser());
            }
        }
    }


}

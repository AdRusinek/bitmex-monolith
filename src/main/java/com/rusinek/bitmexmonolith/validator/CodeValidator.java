package com.rusinek.bitmexmonolith.validator;

import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.model.security.LoginFailure;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import com.rusinek.bitmexmonolith.repositories.security.LoginFailureRepository;
import com.rusinek.bitmexmonolith.security.LoginRequest;
import com.rusinek.bitmexmonolith.services.UserLockService;
import com.rusinek.bitmexmonolith.util.HttpReqRespUtils;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Adrian Rusinek on 13.05.2020
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class CodeValidator implements Validator {

    private final GoogleAuthenticator googleAuthenticator;
    private final UserRepository userRepository;
    private final LoginFailureRepository loginFailureRepository;
    private final HttpReqRespUtils httpReqRespUtils;
    private final UserLockService userLockService;

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void validate(Object target, Errors errors) {

        LoginRequest loginRequest = (LoginRequest) target;

        if (loginRequest.getCode() != null) {
            if (!googleAuthenticator.authorizeUser(loginRequest.getUsername(), loginRequest.getCode())) {

                log.debug("User provided wrong 2FA code.");

                LoginFailure.LoginFailureBuilder builder = LoginFailure.builder();

                userRepository.findByUsername(loginRequest.getUsername()).ifPresent(builder::user);
                builder.username(loginRequest.getUsername());
                builder.sourceIp(httpReqRespUtils.getClientIpAddressIfServletRequestExist());
                LoginFailure failure = loginFailureRepository.save(builder.build());

                log.debug("Failure Event: " + failure.getId());

                if (failure.getUser() != null) {
                    userLockService.lockUserAccount(failure.getUser());
                }

                errors.rejectValue("code", "Wrong", "Niepoprawne dane.");
            }
        }
    }
}

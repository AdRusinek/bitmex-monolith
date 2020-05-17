package com.rusinek.bitmexmonolith.validator;

import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.security.LoginRequest;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Adrian Rusinek on 13.05.2020
 **/
@Component
@RequiredArgsConstructor
public class CodeValidator implements Validator {

    private final GoogleAuthenticator googleAuthenticator;

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
                errors.rejectValue("username", "Wrong", "Credentials are not valid.");
                errors.rejectValue("password", "Wrong", "Credentials are not valid.");
                errors.rejectValue("code", "Wrong", "Credentials are not valid.");
            }
        } else {
            errors.rejectValue("code", "Wrong", "You have to provide Two-Factor token.");
        }
    }
}

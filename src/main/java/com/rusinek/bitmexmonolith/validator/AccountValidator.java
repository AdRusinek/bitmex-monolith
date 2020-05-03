package com.rusinek.bitmexmonolith.validator;

import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.services.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.rusinek.bitmexmonolith.services.ExchangeConstants.API_KEY;

/**
 * Created by Adrian Rusinek on 03.05.2020
 **/
@Component
@RequiredArgsConstructor
public class AccountValidator implements Validator {

    private final ExchangeService exchangeService;

    @Override
    public boolean supports(Class<?> aClass) {
        return Account.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Account account = (Account) target;

        if (!account.getApiKey().isEmpty() && !account.getApiKeySecret().isEmpty()) {
            int status = exchangeService.testConnection(ExchangeService.HTTP_METHOD.GET, API_KEY, account.getApiKey(), account.getApiKeySecret());
            if (status != HttpStatus.OK.value()) {
                errors.rejectValue("apiKey","Connection","Given credentials are not valid.");
                errors.rejectValue("apiKeySecret","Connection","Given credentials are not valid.");
            }
        }
    }
}

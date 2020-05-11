package com.rusinek.bitmexmonolith.validator;

import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.services.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.rusinek.bitmexmonolith.services.ExchangeConstants.GET_APIKEY_URL;

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
            // look for status
            int response = verifyConnection(account);
            //return errors
            manageStatuses(response, errors);
        }
    }

    private int verifyConnection(Account account) {
        return exchangeService
                .testConnection(GET_APIKEY_URL, account.getApiKey(), account.getApiKeySecret(), account.getAccountOwner());
    }

    private void manageStatuses(int response, Errors errors) {
        // response is 0 if limits are exceeded
        if (response == 0) {
            errors.rejectValue("apiKey", "Limit", "Your ability to add account has been blocked for 2 minutes and" +
                    " administration has been notified about your attempts.");
            errors.rejectValue("apiKeySecret", "Limit", "Your ability to add account has been blocked for 2 minutes and" +
                    " administration has been notified about your attempts.");
        }
        // response is 1 if key permissions not order
        if (response == 1) {
            errors.rejectValue("apiKey", "Permission", "Set 'Order' key permission to allow the placement and cancellation of orders.");
            errors.rejectValue("apiKeySecret", "Permission", "Set 'Order' key permission to allow the placement and cancellation of orders.");
        }
        // response is 2 if user unauthorized (wrong credentials)
        if (response == 2) {
            errors.rejectValue("apiKey", "Connection", "Given credentials are not valid.");
            errors.rejectValue("apiKeySecret", "Connection", "Given credentials are not valid.");
        }
    }
}

package com.rusinek.bitmexmonolith.validator;

import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.repositories.AccountRepository;
import com.rusinek.bitmexmonolith.services.exchange.ExchangeService;
import com.rusinek.bitmexmonolith.services.exchange.ParameterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Adrian Rusinek on 03.05.2020
 **/
@Component
@RequiredArgsConstructor
public class AccountValidator implements Validator {

    private final ExchangeService exchangeService;
    private final AccountRepository accountRepository;
    private final ParameterService parameterService;

    @Override
    public boolean supports(Class<?> aClass) {
        return Account.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Account account = (Account) target;

        verifyIfTheSameAccountName(account, errors);

        if (!account.getApiKey().isEmpty() && !account.getApiKeySecret().isEmpty()) {
            // look for status
            int response = verifyConnection(account);
            //return errors
            manageStatuses(response, errors);
        }
    }

    private void verifyIfTheSameAccountName(Account account, Errors errors) {
        accountRepository.findAllByAccountOwner(account.getAccountOwner()).forEach(userAccount -> {
            if (userAccount.getAccountName().equals(account.getAccountName())) {
                errors.rejectValue("accountName", "AlreadyExists", "Account with name: '"
                        + account.getAccountName() + "' was already created by you.");
            }
        });
    }

    private int verifyConnection(Account account) {
        System.out.println(account);
        return exchangeService
                .testConnection("/apiKey", parameterService.fillParamsForGetRequest(ParameterService.RequestContent.GET_API_KEY),
                        account.getApiKey(), account.getApiKeySecret(), account.getAccountOwner());
    }

    private void manageStatuses(int response, Errors errors) {
        switch (response) {
            case 0:
                errors.rejectValue("apiKey", "Limit", "Your ability to add account has been blocked for 2 minutes and" +
                        " administration has been notified about your attempts.");
                errors.rejectValue("apiKeySecret", "Limit", "Your ability to add account has been blocked for 2 minutes and" +
                        " administration has been notified about your attempts.");
                break;
            case 1:
                errors.rejectValue("apiKey", "Permission", "Set 'Order' key permission to allow the placement and cancellation of orders.");
                errors.rejectValue("apiKeySecret", "Permission", "Set 'Order' key permission to allow the placement and cancellation of orders.");
                break;
            case 2:
                errors.rejectValue("apiKey", "Connection", "Given credentials are not valid.");
                errors.rejectValue("apiKeySecret", "Connection", "Given credentials are not valid.");
                break;
            case 3:
                errors.rejectValue("apiKey", "Withdraw", "We do not support keys containing withdraw capability.");
                errors.rejectValue("apiKeySecret", "Withdraw", "We do not support keys containing withdraw capability.");
                break;
        }
    }
}

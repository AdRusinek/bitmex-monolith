package com.rusinek.bitmexmonolith.services;

import com.rusinek.bitmexmonolith.controllers.mappers.AccountMapper;
import com.rusinek.bitmexmonolith.exceptions.MapValidationErrorService;
import com.rusinek.bitmexmonolith.exceptions.accountExceptions.*;
import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.repositories.AccountRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import com.rusinek.bitmexmonolith.validator.AccountValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Created by Adrian Rusinek on 21.02.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final LimitService limitService;
    private final AccountValidator accountValidator;
    private final MapValidationErrorService errorService;

    public ResponseEntity<?> saveAccount(Account account, BindingResult result, Principal principal, AccountMapper accountMapper) {

        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());

        if (!optionalUser.isPresent()) {
            log.error("Error occurred, could not find user '" + principal.getName() + "' while saving account.");
        }

        List<Account> allByAccountOwner = accountRepository.findAllByAccountOwner(principal.getName());

        // checks if account with provided name already exists
        boolean matched = allByAccountOwner.stream()
                .anyMatch(acc -> acc.getAccountName().equals(account.getAccountName()));
        // if account exists throw and error
        if (matched) {
            throw new AccountNameAlreadyExistsException("Account '" + account.getAccountName() + "' already exists.");
        }

        // if to many accounts throw error
        if (allByAccountOwner.size() > 2) {
            throw new AccountAmountException("Accounts amount exceeded.");
        }

        boolean credentialsExist = allByAccountOwner.stream()
                .anyMatch(accForCredentials -> accForCredentials.getApiKey()
                        .equals(account.getApiKey()) && accForCredentials.getApiKeySecret().equals(account.getApiKeySecret()));

        if (credentialsExist) {
            throw new AccountCredentialsException("You already provided Credentials with that Api Key and Api Key Secret.");
        }

        optionalUser.ifPresent(user -> {
            account.setAccountOwner(user.getUsername());
            account.setUser(user);
        });

        //validates if it is possible to make connection with provided credentials
        accountValidator.validate(account, result);
        if (result.hasErrors()) {
            return errorService.validateErrors(result);
        }

        Account savedAccount = accountRepository.save(account);
        limitService.saveAccountRequestLimit(savedAccount);

        return new ResponseEntity<>(accountMapper.accountToDto(savedAccount), HttpStatus.CREATED);
    }

    public List<Account> getAllAccounts(String username) {
        return accountRepository.findAllByAccountOwner(username);
    }

    Account findByAccountIdAndOwner(Long id, String userName) {
        // validating if exchange account belongs to your user account
        Optional<Account> account = accountRepository.findByAccountOwnerAndId(userName, id);

        if (!account.isPresent()) {
            throw new AccountIdException("Account ID '" + id + "' does not exist");
        }

        if (!account.get().getAccountOwner().equals(userName)) {
            throw new AccountNotFoundException("Account not found on your User");
        }
        return account.get();
    }

    public void deleteAccount(String accountId, Principal principal) {

        Optional<Account> optionalAccount = accountRepository.findByAccountOwnerAndId(principal.getName(), Long.valueOf(accountId));

        if (!optionalAccount.isPresent()) {
            throw new AccountIdException("Account with id '" + accountId + "' does not exists on this User dashboard.");
        }

        optionalAccount.ifPresent(accountRepository::delete);
    }
}

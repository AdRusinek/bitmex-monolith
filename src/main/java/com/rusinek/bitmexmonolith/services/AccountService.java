package com.rusinek.bitmexmonolith.services;

import com.rusinek.bitmexmonolith.controllers.mappers.AccountMapper;
import com.rusinek.bitmexmonolith.dto.AccountDto;
import com.rusinek.bitmexmonolith.exceptions.MapValidationErrorService;
import com.rusinek.bitmexmonolith.exceptions.accountExceptions.AccountIdException;
import com.rusinek.bitmexmonolith.exceptions.accountExceptions.AccountNameAlreadyExistsException;
import com.rusinek.bitmexmonolith.exceptions.accountExceptions.AccountNotFoundException;
import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.repositories.AccountRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import com.rusinek.bitmexmonolith.validator.AccountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Adrian Rusinek on 21.02.2020
 **/
@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final MapValidationErrorService errorService;
    private final AccountMapper accountMapper;
    private final AccountValidator accountValidator;

    public ResponseEntity<?> saveAccount(Account account, BindingResult result, Principal principal) {

        //validates if it is possible to make connection with provided credentials
        accountValidator.validate(account, result);

        if (result.hasErrors()) return errorService.validateErrors(result);

        // checks if account with provided name already exists
        boolean matched = accountRepository.findAllByAccountOwner(principal.getName()).stream()
                .anyMatch(acc -> acc.getAccountName().equals(account.getAccountName()));

        // if account exists throw and error
        if (matched) {
            throw new AccountNameAlreadyExistsException("Account '" + account.getAccountName() + "' already exists.");
        }

        // else save to the db and return dto
        userRepository.findByUsername(principal.getName()).ifPresent(user -> {
            account.setAccountOwner(user.getUsername());
            account.setUser(user);
        });

        return new ResponseEntity<>(accountMapper.accountToDto(accountRepository.save(account)), HttpStatus.CREATED);
    }

    public List<AccountDto> getAllAccounts(String username) {
        // returns all accounts but without some not necessary properties
        return accountRepository.findAllByAccountOwner(username)
                .stream().map(accountMapper::accountToDto)
                .collect(Collectors.toList());
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

}

package com.rusinek.bitmexmonolith.services.credentials;

import com.rusinek.bitmexmonolith.controllers.mappers.AccountMapper;
import com.rusinek.bitmexmonolith.dto.AccountDto;
import com.rusinek.bitmexmonolith.exceptions.MapValidationErrorService;
import com.rusinek.bitmexmonolith.exceptions.accountExceptions.AccountIdException;
import com.rusinek.bitmexmonolith.exceptions.accountExceptions.AccountNotFoundException;
import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.repositories.AccountRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
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
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final MapValidationErrorService mapValidationErrorService;
    private final AccountMapper accountMapper;

    @Override
    public ResponseEntity<?> saveAccount(Account account, BindingResult result, Principal principal) {

        ResponseEntity<?> errorMap = mapValidationErrorService.validateErrors(result);
        if (errorMap != null) return errorMap;

        if (!getAllAccounts(principal.getName()).contains(account)) {
            User user = userRepository.findByUsername(principal.getName());
            account.setAccountOwner(user.getUsername());
            account.setUser(user);

            return new ResponseEntity<>(accountRepository.save(account), HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Account already exists.", HttpStatus.BAD_REQUEST);
    }

    @Override
    public List<AccountDto> getAllAccounts(String username) {
       return accountRepository.findAllByAccountOwner(username)
               .stream().map(accountMapper::accountToDto).collect(Collectors.toList());
    }


    @Override
    public Account findByAccountId(Long id, String userName) {
        Optional<Account> credential = accountRepository.findByAccountOwnerAndId(userName, id);

        if (!credential.isPresent()) {
            throw new AccountIdException("Account ID '" + id + "' does not exist");
        }

        if (!credential.get().getAccountOwner().equals(userName)) {
            throw new AccountNotFoundException("Account not found on your User Page");
        }

        return credential.get();
    }

}

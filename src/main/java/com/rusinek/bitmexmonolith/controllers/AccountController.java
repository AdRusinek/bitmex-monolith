package com.rusinek.bitmexmonolith.controllers;


import com.rusinek.bitmexmonolith.controllers.mappers.AccountMapper;
import com.rusinek.bitmexmonolith.dto.AccountDto;
import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Adrian Rusinek on 08.03.2020
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @PostMapping("/add")
    public ResponseEntity<?> addAccountForUser(@Valid @RequestBody Account account, BindingResult result, Principal principal) {

        return accountService.saveAccount(account, result, principal, accountMapper);
    }

    @GetMapping("/get-accounts")
    public List<AccountDto> getAccountNames(Principal principal) {

        return accountService.getAllAccounts(principal.getName())
                .stream().map(accountMapper::accountToDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> deleteAlert(@PathVariable String accountId, Principal principal) {

        accountService.deleteAccount(accountId, principal);

        return new ResponseEntity<>("Account wth id '" + accountId + "' was deleted successfully.", HttpStatus.OK);
    }
}

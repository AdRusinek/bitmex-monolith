package com.rusinek.bitmexmonolith.controllers;


import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.services.credentials.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by Adrian Rusinek on 08.03.2020
 **/
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("api/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/add")
    public ResponseEntity<?> addAccountForUser(@Valid @RequestBody Account account,
                                               BindingResult result,
                                               Principal principal) {
        return accountService.saveAccount(account, result, principal);
    }

    @GetMapping("/get-accounts")
    public Iterable<Account> getAccounts(Principal principal) {
        return accountService.getAllAccounts(principal.getName());
    }
}

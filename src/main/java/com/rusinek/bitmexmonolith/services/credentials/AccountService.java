package com.rusinek.bitmexmonolith.services.credentials;

import com.rusinek.bitmexmonolith.dto.AccountDto;
import com.rusinek.bitmexmonolith.model.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.List;

public interface AccountService {

    ResponseEntity<?> saveAccount(Account account, BindingResult result, Principal principal);

    List<AccountDto> getAllAccounts(String username);

    Account findByAccountId(Long id, String userName);
}

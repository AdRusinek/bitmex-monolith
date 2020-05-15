package com.rusinek.bitmexmonolith.services;


import com.rusinek.bitmexmonolith.controllers.mappers.TrailingStopMapper;
import com.rusinek.bitmexmonolith.dto.TrailingStopDto;
import com.rusinek.bitmexmonolith.exceptions.MapValidationErrorService;
import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.model.TrailingStop;
import com.rusinek.bitmexmonolith.repositories.TrailingStopRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Adrian Rusinek on 25.02.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class TrailingStopService {

    private final TrailingStopRepository trailingStopRepository;
    private final UserRepository userRepository;
    private final AccountService accountService;
    private final TrailingStopMapper trailingStopMapper;
    private final MapValidationErrorService errorService;

    public ResponseEntity<?> saveTrailingStop(TrailingStop trailingStop, BindingResult result, Principal principal, String accountId) {

        // processErrorResponse possible errors that may come from model annotations
        if (result.hasErrors()) return errorService.validateErrors(result);

        // checks if the user exists and after it finds it will bind trailing stop to specific account
        userRepository.findByUsername(principal.getName()).ifPresent(user -> {
            trailingStop.setTrailingStopOwner(user.getUsername());
            Account account = accountService.findByAccountIdAndOwner(Long.valueOf(accountId), principal.getName());
            trailingStop.setAccount(account);
            trailingStop.setTrailingStopOwner(account.getAccountOwner());
        });
        return new ResponseEntity<>(trailingStopMapper.trailingStopToDto(trailingStopRepository.save(trailingStop)), HttpStatus.CREATED);
    }

    public ResponseEntity<List<TrailingStopDto>> getAllByOwnerAndAccountId(Principal principal, String accountId) {
        // gets all trailing stops bind to specific user
        List<TrailingStop> allByTrailingStopOwner = (List<TrailingStop>)
                trailingStopRepository.findAllByTrailingStopOwner(principal.getName());

        // returns trailing stops that belongs to specific account
        return new ResponseEntity<>(allByTrailingStopOwner.stream().filter(trailingStop ->
                trailingStop.getAccount().getId().equals(Long.valueOf(accountId)))
                .map(trailingStopMapper::trailingStopToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

}

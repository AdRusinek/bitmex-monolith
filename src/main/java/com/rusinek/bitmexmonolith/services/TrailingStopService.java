package com.rusinek.bitmexmonolith.services;


import com.rusinek.bitmexmonolith.controllers.mappers.TrailingStopMapper;
import com.rusinek.bitmexmonolith.dto.TrailingStopDto;
import com.rusinek.bitmexmonolith.exceptions.MapValidationErrorService;
import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.model.TrailingStop;
import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.repositories.TrailingStopRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import com.rusinek.bitmexmonolith.services.AccountService;
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

        if(result.hasErrors()) return errorService.validateErrors(result);

        if (!getAllTrailingStops(principal.getName()).contains(trailingStop)) {
            User user = userRepository.findByUsername(principal.getName());
            trailingStop.setTrailingStopOwner(user.getUsername());
            Account account = accountService.findByAccountId(Long.valueOf(accountId), principal.getName());
            trailingStop.setAccount(account);
            trailingStop.setUser(user);

            return new ResponseEntity<>(trailingStopMapper.trailingStopToDto(trailingStopRepository.save(trailingStop)), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new TrailingStop(), HttpStatus.BAD_REQUEST);
    }

    private List<TrailingStop> getAllTrailingStops(String trailingStopOwner) {
        return (List<TrailingStop>) trailingStopRepository.findAllByTrailingStopOwner(trailingStopOwner);
    }

    public void deleteTrailingStop(TrailingStop trailingStop) {
        trailingStopRepository.delete(trailingStop);
    }


    public ResponseEntity<List<TrailingStopDto>> getAllTrailingStopsByCredentialsId(Principal principal, String credentialsId) {
        List<TrailingStop> allByTrailingStopOwner = (List<TrailingStop>)
                trailingStopRepository.findAllByTrailingStopOwner(principal.getName());

        return new ResponseEntity<>(allByTrailingStopOwner.stream().filter(trailingStop ->
                trailingStop.getAccount().getId().equals(Long.valueOf(credentialsId)))
                .map(trailingStopMapper::trailingStopToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public Iterable<TrailingStop> findAll() {
        return trailingStopRepository.findAll();
    }
}

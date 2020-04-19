package com.rusinek.bitmexmonolith.services.trailings;


import com.rusinek.bitmexmonolith.exceptions.MapValidationErrorService;
import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.model.TrailingStop;
import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.repositories.TrailingStopRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import com.rusinek.bitmexmonolith.services.credentials.AccountService;
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
@Service
@Slf4j
public class TrailingStopServiceImpl implements TrailingStopService {

    private TrailingStopRepository trailingStopRepository;
    private UserRepository userRepository;
    private AccountService accountService;
    private MapValidationErrorService errorService;

    public TrailingStopServiceImpl(TrailingStopRepository trailingStopRepository, UserRepository userRepository,
                                   AccountService accountService, MapValidationErrorService errorService) {
        this.trailingStopRepository = trailingStopRepository;
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.errorService = errorService;
    }

    @Override
    public ResponseEntity<?> saveTrailingStop(TrailingStop trailingStop, BindingResult result, Principal principal, String accountId) {

        ResponseEntity<?> errorMap = errorService.validateErrors(result);
        if (errorMap != null) return errorMap;

        if (!getAllTrailingStops(principal.getName()).contains(trailingStop)) {
            User user = userRepository.findByUsername(principal.getName());
            trailingStop.setTrailingStopOwner(user.getUsername());
            Account account = accountService.findByAccountId(Long.valueOf(accountId), principal.getName());
            trailingStop.setAccount(account);
            trailingStop.setUser(user);

            return new ResponseEntity<>(trailingStopRepository.save(trailingStop), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new TrailingStop(), HttpStatus.BAD_REQUEST);
    }

    @Override
    public List<TrailingStop> getAllTrailingStops(String trailingStopOwner) {
        return (List<TrailingStop>) trailingStopRepository.findAllByTrailingStopOwner(trailingStopOwner);
    }

    @Override
    public void deleteTrailingStop(TrailingStop trailingStop) {
        trailingStopRepository.delete(trailingStop);
    }


    //teraz przechwytywanie zlych credentiali
    @Override
    public ResponseEntity<List<TrailingStop>> getAllTrailingStopsByCredentialsId(Principal principal, String credentialsId) {
        List<TrailingStop> allByTrailingStopOwner = (List<TrailingStop>)
                trailingStopRepository.findAllByTrailingStopOwner(principal.getName());

        return new ResponseEntity<>(allByTrailingStopOwner.stream().filter(trailingStop ->
                trailingStop.getAccount().getId().equals(Long.valueOf(credentialsId)))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @Override
    public Iterable<TrailingStop> findAll() {
        return trailingStopRepository.findAll();
    }
}

package com.rusinek.bitmexmonolith.controllers;

import com.rusinek.bitmexmonolith.controllers.mappers.TrailingStopMapper;
import com.rusinek.bitmexmonolith.exceptions.MapValidationErrorService;
import com.rusinek.bitmexmonolith.model.TrailingStop;
import com.rusinek.bitmexmonolith.services.TrailingStopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by Adrian Rusinek on 29.02.2020
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trailing-stops")
public class TrailingStopController {

    private final TrailingStopService trailingStopService;
    private final TrailingStopMapper trailingStopMapper;
    private final MapValidationErrorService errorService;


    @PostMapping("/set-trailing/{accountId}")
    public ResponseEntity<?> setTrailingStop(@Valid @RequestBody TrailingStop trailingStop, BindingResult result, Principal principal,
                                             @PathVariable String accountId) {

        if (result.hasErrors()) {
            return errorService.validateErrors(result);
        }

        return new ResponseEntity<>(trailingStopMapper.trailingStopToDto(trailingStopService
                .saveTrailingStop(trailingStop, principal, accountId)), HttpStatus.OK);
    }

    @GetMapping("/get-waiting-trailing-stops/{accountId}")
    public ResponseEntity<?> getWaitingTrailingStops(Principal principal, @PathVariable String accountId) {

        return new ResponseEntity<>(trailingStopService.getAllByOwnerAndAccountId(principal, accountId)
                .stream().map(trailingStopMapper::trailingStopToDto), HttpStatus.OK);
    }

    @DeleteMapping("/{accountId}/{trailingId}")
    public ResponseEntity<?> deleteTrailingStop(@PathVariable String accountId, @PathVariable String trailingId, Principal principal) {

        trailingStopService.deleteTrailingStop(accountId, trailingId, principal);

        return new ResponseEntity<>("Trailing wth id '" + trailingId + "' was deleted successfully " +
                "from account " + accountId + ".", HttpStatus.OK);
    }
}

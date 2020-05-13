package com.rusinek.bitmexmonolith.controllers;

import com.rusinek.bitmexmonolith.model.TrailingStop;
import com.rusinek.bitmexmonolith.services.TrailingStopService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/set-trailing/{accountId}")
    public ResponseEntity<?> setTrailingStop(@Valid @RequestBody TrailingStop trailingStop,
                                             BindingResult result,
                                             Principal principal,
                                             @PathVariable String accountId) {
        return trailingStopService.saveTrailingStop(trailingStop, result, principal, accountId);
    }

    @GetMapping("/get-waiting-trailing-stops/{accountId}")
    public ResponseEntity<?> getWaitingTrailingStops(Principal principal,
                                                     @PathVariable String accountId) {
        return trailingStopService.getAllByOwnerAndAccountId(principal, accountId);
    }
}

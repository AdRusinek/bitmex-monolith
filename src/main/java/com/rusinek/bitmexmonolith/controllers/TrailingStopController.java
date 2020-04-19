package com.rusinek.bitmexmonolith.controllers;

import com.rusinek.bitmexmonolith.model.TrailingStop;
import com.rusinek.bitmexmonolith.services.trailings.TrailingStopService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by Adrian Rusinek on 29.02.2020
 **/
@RestController
@RequestMapping("/api/trailing-stops")
@CrossOrigin
public class TrailingStopController {

    private TrailingStopService trailingStopService;

    public TrailingStopController(TrailingStopService trailingStopService) {
        this.trailingStopService = trailingStopService;
    }

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
        return trailingStopService.getAllTrailingStopsByCredentialsId(principal, accountId);
    }
}

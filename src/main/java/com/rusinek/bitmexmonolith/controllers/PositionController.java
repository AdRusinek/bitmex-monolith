package com.rusinek.bitmexmonolith.controllers;


import com.rusinek.bitmexmonolith.services.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.rusinek.bitmexmonolith.services.ExchangeConstants.OPEN_POSITION;

/**
 * Created by Adrian Rusinek on 23.02.2020
 **/
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/positions")
public class PositionController {

    private final PositionService positionService;

    @GetMapping("/get-positions/{accountId}")
    public ResponseEntity<?> getOrders(@PathVariable String accountId, Principal principal) {
        return positionService.requestPositions(accountId, principal, OPEN_POSITION);
    }
}

package com.rusinek.bitmexmonolith.controllers;


import com.rusinek.bitmexmonolith.services.exchange.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by Adrian Rusinek on 23.02.2020
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/positions")
public class PositionController {

    private final PositionService positionService;

    @GetMapping("/get-positions/{accountId}")
    public ResponseEntity<?> getPositions(@PathVariable String accountId, Principal principal) {
        return positionService.requestPositions(accountId, principal);
    }
}

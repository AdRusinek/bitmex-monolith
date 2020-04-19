package com.rusinek.bitmexmonolith.controllers;


import com.rusinek.bitmexmonolith.services.positions.PositionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by Adrian Rusinek on 23.02.2020
 **/
@RestController
@RequestMapping("/api/positions")
@CrossOrigin
public class PositionController {

    private PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping("/get-positions/{accountId}")
    public ResponseEntity<?> getOrders(@PathVariable String accountId, Principal principal) {
        return positionService.requestPositions(accountId, principal);
    }
}

package com.rusinek.bitmexmonolith.controllers;


import com.rusinek.bitmexmonolith.controllers.mappers.PositionMapper;
import com.rusinek.bitmexmonolith.services.exchange.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created by Adrian Rusinek on 23.02.2020
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/positions")
public class PositionController {

    private final PositionService positionService;
    private final PositionMapper positionMapper;

    @GetMapping("/get-positions/{accountId}")
    public ResponseEntity<?> getPositions(@PathVariable String accountId, Principal principal) {

        return new ResponseEntity<>(positionMapper
                .positionsToDto(positionService.requestPositions(accountId, principal)), HttpStatus.OK);

    }
}

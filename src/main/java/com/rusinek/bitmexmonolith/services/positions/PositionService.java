package com.rusinek.bitmexmonolith.services.positions;

import org.springframework.http.ResponseEntity;

import java.security.Principal;

/**
 * Created by Adrian Rusinek on 23.02.2020
 **/
public interface PositionService {

    ResponseEntity<String> requestPositions(String accountId, Principal principal);
}

package com.rusinek.bitmexmonolith.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;

import static com.rusinek.bitmexmonolith.services.ExchangeService.HTTP_METHOD.GET;

/**
 * Created by Adrian Rusinek on 23.02.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class PositionService {

    private final ExchangeService exchangeService;

    public ResponseEntity<?> requestPositions(String accountId, Principal principal, String positionUrl) {
        try {
            ArrayList positionList = (ArrayList) exchangeService.requestApi(GET, positionUrl, Long.valueOf(accountId), principal.getName());
            if (positionList == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            log.debug("Getting position from BitMEX");
            return new ResponseEntity<>(positionList, HttpStatus.OK);
        } catch (ClassCastException ex) {
            log.error("Failure requesting position from BitMEX.");
            ex.getMessage();
        }
        return new ResponseEntity<>(new JSONArray().toString(), HttpStatus.ACCEPTED);
    }
}

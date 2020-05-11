package com.rusinek.bitmexmonolith.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mashape.unirest.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rusinek.bitmexmonolith.exceptions.BitmexExceptionService;
import com.rusinek.bitmexmonolith.model.response.Position;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

/**
 * Created by Adrian Rusinek on 23.02.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class PositionService {

    private final ExchangeService exchangeService;
    private final BitmexExceptionService bitmexExceptionService;
    private final ObjectMapper objectMapper;

    // returns position but if there is some kind of error in deserialization then it tries to bind it to the BitMEX error model response
    public ResponseEntity<?> requestPositions(String accountId, Principal principal, String positionUrl) {

        HttpResponse<String> response = exchangeService.requestApiWithGet(positionUrl, Long.valueOf(accountId), principal.getName());
        try {
            List<Position> positions = objectMapper.readValue(response.getBody(), new TypeReference<List<Position>>() {
            });
            return new ResponseEntity<>(positions, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            bitmexExceptionService.processErrorResponse(objectMapper, response);
        }
        return null;
    }
}

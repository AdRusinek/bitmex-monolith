package com.rusinek.bitmexmonolith.services.exchange;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.rusinek.bitmexmonolith.controllers.mappers.PositionMapper;
import com.rusinek.bitmexmonolith.exceptions.BitMEXExceptionService;
import com.rusinek.bitmexmonolith.exceptions.exchangeExceptions.ExchangeLimitsException;
import com.rusinek.bitmexmonolith.model.response.Position;
import com.rusinek.bitmexmonolith.util.ParameterService;
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
    private final ParameterService parameterService;
    private final ObjectMapper objectMapper;

    public List<Position> requestPositions(String accountId, Principal principal) {

        HttpResponse<String> response = exchangeService.requestApi(ExchangeService.HttpMethod.GET, "/position",
                parameterService.fillParamsForGetRequest(ParameterService.RequestContent.GET_POSITIONS), Long.valueOf(accountId), principal.getName());
        try {
            return objectMapper.readValue(response.getBody(), new TypeReference<List<Position>>() {
            });
        } catch (Exception e) {
            throw new ExchangeLimitsException("BitMEX is currently not ready to use or you exceeded api limits.");
        }
    }
}

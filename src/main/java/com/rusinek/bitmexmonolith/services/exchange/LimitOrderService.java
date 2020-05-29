package com.rusinek.bitmexmonolith.services.exchange;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.rusinek.bitmexmonolith.controllers.mappers.OrderLimitMapper;
import com.rusinek.bitmexmonolith.exceptions.BitMEXExceptionService;
import com.rusinek.bitmexmonolith.model.response.Order;
import com.rusinek.bitmexmonolith.util.ParameterService;
import com.rusinek.bitmexmonolith.util.ResponseModifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;


/**
 * Created by Adrian Rusinek on 21.02.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class LimitOrderService {

    private final ExchangeService exchangeService;
    private final BitMEXExceptionService bitMEXExceptionService;
    private final ParameterService parameterService;
    private final ObjectMapper objectMapper;
    private final ResponseModifier responseModifier;
    private final OrderLimitMapper orderLimitMapper;

    public ResponseEntity<?> requestLimitOrders(Principal principal, String accountId) {

        HttpResponse<String> response = exchangeService.requestApi(ExchangeService.HttpMethod.GET,
                "/order", parameterService
                        .fillParamsForGetRequest(ParameterService.RequestContent.GET_LIMIT_ORDERS),
                Long.valueOf(accountId), principal.getName());

        try {
            return new ResponseEntity<>(orderLimitMapper.orderLimitToDto(responseModifier
                    .extractAndSetNewQuantity(objectMapper.readValue(response.getBody(), new TypeReference<List<Order>>() {
                    }))), HttpStatus.OK);
        } catch (Exception e) {
            return bitMEXExceptionService.respondAndInform();
        }
    }
}

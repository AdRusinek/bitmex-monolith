package com.rusinek.bitmexmonolith.services.exchange;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.rusinek.bitmexmonolith.controllers.mappers.OrderStopMapper;
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
 * Created by Adrian Rusinek on 12.05.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class StopOrderService {

    private final ExchangeService exchangeService;
    private final ParameterService parameterService;
    private final ObjectMapper objectMapper;
    private final OrderStopMapper orderStopMapper;
    private final ResponseModifier responseModifier;
    private final BitMEXExceptionService bitMEXExceptionService;

    public ResponseEntity<?> requestStopOrders(Principal principal, String accountId) {

        HttpResponse<String> response = exchangeService.requestApi(ExchangeService.HttpMethod.GET, "/order",
                parameterService.fillParamsForGetRequest(ParameterService.RequestContent.GET_STOP_ORDERS), Long.valueOf(accountId), principal.getName());

        // returns list with modified price and quantity
        try {
            return new ResponseEntity<>(responseModifier.extractAndSetNewOrderPrice(orderStopMapper
                    .orderStopsToDtos(responseModifier.extractAndSetNewQuantity(objectMapper
                            .readValue(response.getBody(), new TypeReference<List<Order>>() {
                            })))), HttpStatus.OK);
        } catch (Exception e) {
            return bitMEXExceptionService.respondAndInform();
        }
    }
}

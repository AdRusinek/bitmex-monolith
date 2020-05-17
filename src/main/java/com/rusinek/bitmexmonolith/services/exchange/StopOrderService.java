package com.rusinek.bitmexmonolith.services.exchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.rusinek.bitmexmonolith.controllers.mappers.OrderStopMapper;
import com.rusinek.bitmexmonolith.exceptions.BitmexExceptionService;
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
    private final BitmexExceptionService bitmexExceptionService;
    private final ObjectMapper objectMapper;
    private final OrderStopMapper orderStopMapper;
    private final ResponseModifier responseModifier;

    public ResponseEntity<?> requestStopOrders(Principal principal, String accountId) {

        HttpResponse<String> response = exchangeService.requestApi(ExchangeService.HttpMethod.GET, "/order",
                parameterService.fillParamsForGetRequest(ParameterService.RequestContent.GET_STOP_ORDERS), Long.valueOf(accountId), principal.getName());
        try {
            List<Order> orders = objectMapper.readValue(response.getBody(), new TypeReference<>() {
            });

            // returns list with modified price and quantity
            return new ResponseEntity<>(responseModifier.extractAndSetNewOrderPrice(orderStopMapper
                    .orderStopsToDtos(responseModifier.extractAndSetNewQuantity(orders))), HttpStatus.OK);

        } catch (JsonProcessingException e) {
            bitmexExceptionService.processErrorResponse(objectMapper, response, principal.getName(), accountId);
        }
        return null;
    }
}

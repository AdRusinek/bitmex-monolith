package com.rusinek.bitmexmonolith.services.exchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.rusinek.bitmexmonolith.controllers.mappers.OrderStopMapper;
import com.rusinek.bitmexmonolith.exceptions.BitmexExceptionService;
import com.rusinek.bitmexmonolith.model.response.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adrian Rusinek on 12.05.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderStopService {

    private final ExchangeService exchangeService;
    private final BitmexExceptionService bitmexExceptionService;
    private final ObjectMapper objectMapper;
    private final OrderStopMapper orderStopMapper;
    private final ResponseModifier responseModifier;

    public ResponseEntity<?> requestStopOrders(Principal principal, String accountId) {

        Gson gson = new Gson();
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> filterMap = new HashMap<>();
        String[] ordTypes = {"Stop", "MarketIfTouched", "StopLimit", "LimitIfTouched"};
        filterMap.put("symbol", "XBTUSD");
        filterMap.put("ordType", ordTypes);
        filterMap.put("open", true);

        params.put("symbol", "XBT");
        params.put("filter", gson.toJson(filterMap));
        params.put("count", 20);
        params.put("reverse", false);

        HttpResponse<String> response = exchangeService.requestApi(ExchangeService.HTTP_METHOD.GET, "/order",
                params, Long.valueOf(accountId), principal.getName());
        try {
            List<Order> orders = objectMapper.readValue(response.getBody(), new TypeReference<List<Order>>() {
            });

            // returns list with modified price and quantity
            return new ResponseEntity<>(responseModifier.extractAndSetNewOrderPrice(orderStopMapper
                    .orderStopToDto(responseModifier.extractAndSetNewQuantity(orders))), HttpStatus.OK);

        } catch (JsonProcessingException e) {
            bitmexExceptionService.processErrorResponse(objectMapper, response, principal.getName(), accountId);
        }
        return null;
    }
}

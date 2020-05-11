package com.rusinek.bitmexmonolith.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.internal.LinkedTreeMap;
import com.mashape.unirest.http.HttpResponse;
import com.rusinek.bitmexmonolith.exceptions.BitmexExceptionService;
import com.rusinek.bitmexmonolith.model.response.Order;
import com.rusinek.bitmexmonolith.model.response.Position;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Adrian Rusinek on 21.02.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ExchangeService exchangeService;
    private final BitmexExceptionService bitmexExceptionService;
    private final ObjectMapper objectMapper;

    private JSONArray setCorrectPrice(List<LinkedTreeMap> orders) {
        JSONArray array = new JSONArray();
        orders.forEach(order -> {
            JSONObject jsonObject = new JSONObject(order);
            String side = jsonObject.getString("side");
            if (side.equalsIgnoreCase("Sell")) {
                double orderQty = jsonObject.getDouble("orderQty");
                String finalOrderQty = "-" + orderQty;
                jsonObject.put("orderQty", finalOrderQty);
            }
            array.put(jsonObject);
        });
        return array;
    }

    // todo teraz sprawdz sobie czy buy czy sell i nie daj minusa albo daj ;)
    public ResponseEntity<?> requestOrders(Principal principal, String accountId, String orderType) {
        HttpResponse<String> response = exchangeService.requestApiWithGet(orderType, Long.valueOf(accountId), principal.getName());
        try {
            List<Order> orders = objectMapper.readValue(response.getBody(), new TypeReference<List<Order>>() {
            });
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            bitmexExceptionService.processErrorResponse(objectMapper, response);
        }
        return null;
    }
}

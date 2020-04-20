package com.rusinek.bitmexmonolith.services;

import com.google.gson.internal.LinkedTreeMap;
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

import static com.rusinek.bitmexmonolith.services.ExchangeService.HTTP_METHOD.GET;


/**
 * Created by Adrian Rusinek on 21.02.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ExchangeService exchangeService;

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

    public ResponseEntity<?> requestOrders(Principal principal, String accountId, String orderType) {
        try {
            ArrayList orders = (ArrayList) exchangeService.requestApi(GET, orderType, Long.valueOf(accountId), principal.getName());
            if (orders == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            log.debug("Getting orders from BitMEX.");
            return new ResponseEntity<>(setCorrectPrice(orders).toString(), HttpStatus.OK);
        } catch (ClassCastException ex) {
            log.error("Failure requesting orders from BitMEX.");
            ex.getMessage();
        }
        return new ResponseEntity<>(new ArrayList<LinkedTreeMap>().toString(), HttpStatus.ACCEPTED);
    }
}

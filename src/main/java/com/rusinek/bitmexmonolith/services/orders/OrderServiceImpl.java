package com.rusinek.bitmexmonolith.services.orders;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.rusinek.bitmexmonolith.services.exchange.ExchangeService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rusinek.bitmexmonolith.services.exchange.ExchangeService.HTTP_METHOD.GET;


/**
 * Created by Adrian Rusinek on 21.02.2020
 **/
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private ExchangeService exchangeService;

    public OrderServiceImpl(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

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

    @Override
    public ResponseEntity<?> requestOrders(String status, String type, String accountId, Principal principal) {

        Gson gson = new Gson();
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("ordType", type);
        if (status.equalsIgnoreCase("filled")) {
            filterMap.put("ordStatus", "Filled");
        } else if (status.equalsIgnoreCase("new")) {
            filterMap.put("ordStatus", "New");
        }
        params.put("filter", gson.toJson(filterMap));

        try {
            @SuppressWarnings("unchecked")
            List<LinkedTreeMap> orders = (List<LinkedTreeMap>) exchangeService.requestApi(GET, "/order", params, Long.valueOf(accountId), principal.getName());
            if (orders == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(setCorrectPrice(orders).toString(), HttpStatus.OK);
        } catch (ClassCastException ex) {
            log.error("Failure requesting orders from BitMEX.");
            ex.getMessage();
        }
        return new ResponseEntity<>(new ArrayList<LinkedTreeMap>().toString(), HttpStatus.ACCEPTED);
    }
}

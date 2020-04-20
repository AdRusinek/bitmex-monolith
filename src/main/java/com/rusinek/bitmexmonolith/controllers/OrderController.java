package com.rusinek.bitmexmonolith.controllers;


import com.rusinek.bitmexmonolith.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.rusinek.bitmexmonolith.services.ExchangeConstants.OPENED_LIMIT_ORDERS;
import static com.rusinek.bitmexmonolith.services.ExchangeConstants.OPENED_STOP_ORDERS;

/**
 * Created by Adrian Rusinek on 19.02.2020
 **/
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/get-limit-orders/{accountId}")
    public ResponseEntity<?> getLimitOrders(@PathVariable String accountId, Principal principal) {
        return orderService.requestOrders(principal, accountId, OPENED_LIMIT_ORDERS);
    }

    @GetMapping("/get-stop-orders/{accountId}")
    public ResponseEntity<?> getStopOrders(Principal principal, @PathVariable String accountId) {
        return orderService.requestOrders(principal, accountId, OPENED_STOP_ORDERS);
    }
}

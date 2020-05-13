package com.rusinek.bitmexmonolith.controllers;


import com.rusinek.bitmexmonolith.services.exchange.LimitOrderService;
import com.rusinek.bitmexmonolith.services.exchange.StopOrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by Adrian Rusinek on 19.02.2020
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final LimitOrderService limitOrderService;
    private final StopOrderService stopOrderService;

    @GetMapping("/get-limit-orders/{accountId}")
    public ResponseEntity<?> getLimitOrders(@PathVariable String accountId, Principal principal) {
        return limitOrderService.requestLimitOrders(principal, accountId);
    }

    @GetMapping("/get-stop-orders/{accountId}")
    public ResponseEntity<?> getStopOrders(Principal principal, @PathVariable String accountId) {
        return stopOrderService.requestStopOrders(principal, accountId);
    }
}

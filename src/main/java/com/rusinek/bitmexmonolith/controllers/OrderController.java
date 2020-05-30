package com.rusinek.bitmexmonolith.controllers;


import com.rusinek.bitmexmonolith.controllers.mappers.OrderLimitMapper;
import com.rusinek.bitmexmonolith.controllers.mappers.OrderStopMapper;
import com.rusinek.bitmexmonolith.services.exchange.LimitOrderService;
import com.rusinek.bitmexmonolith.services.exchange.StopOrderService;
import com.rusinek.bitmexmonolith.util.ResponseModifier;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final OrderLimitMapper orderLimitMapper;
    private final OrderStopMapper orderStopMapper;
    private final ResponseModifier responseModifier;

    @GetMapping("/get-limit-orders/{accountId}")
    public ResponseEntity<?> getLimitOrders(@PathVariable String accountId, Principal principal) {

        return new ResponseEntity<>(orderLimitMapper.orderLimitToDto(limitOrderService.requestLimitOrders(principal, accountId)), HttpStatus.OK);

    }

    @GetMapping("/get-stop-orders/{accountId}")
    public ResponseEntity<?> getStopOrders(Principal principal, @PathVariable String accountId) {

        return new ResponseEntity<>(responseModifier.extractAndSetNewOrderPrice(orderStopMapper
                .orderStopsToDtos(stopOrderService.requestStopOrders(principal, accountId, responseModifier))), HttpStatus.OK);
    }
}
